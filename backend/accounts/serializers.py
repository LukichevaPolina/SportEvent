
from multiprocessing import Event
from django.contrib import auth
from django.contrib.auth.tokens import PasswordResetTokenGenerator
from django.utils.encoding import smart_str, force_str, smart_bytes, DjangoUnicodeDecodeError
from django.utils.http import urlsafe_base64_decode, urlsafe_base64_encode 
from rest_framework import serializers
from rest_framework.exceptions import AuthenticationFailed
from rest_framework_simplejwt.tokens import RefreshToken, TokenError
from .models import User
from sports.models import Sport

class RegisterSerializer(serializers.ModelSerializer):
    """
    Register Serializer.

    """
    password = serializers.CharField(max_length=68, min_length=6, write_only=True)

    default_error_messages = {
        'username': 'The username should only contain alphanumeric characters'}

    class Meta:
        model = User
        fields = ['email', 'username', 'password', 'name', 'surname', 'birthday', 'country', 'locality', 'favorite_sports']

    def validate(self, attrs):
        email = attrs.get('email', '')
        username = attrs.get('username', '')

        if not username.isalnum():
            raise serializers.ValidationError(self.default_error_messages)
        
        return attrs

    
    def create(self, validated_data):
        user = User.objects.create_user(**validated_data)
        return user


class EmailVerificationSerializer(serializers.ModelSerializer):
    """
    Email verification serializer.

    """
    token = serializers.CharField(max_length=555)
    redirect_url = serializers.CharField(max_length=555)
    class Meta:
        model = User
        fields = ['token', 'redirect_url']


class LoginSerializer(serializers.ModelSerializer):
    """
    Login serializer.

    """
    email = serializers.EmailField(max_length=255, min_length=3)
    password = serializers.CharField(max_length=68, min_length=3, write_only=True)
    username = serializers.CharField(max_length=255, min_length=6, read_only=True)
    name = serializers.CharField(max_length=68, min_length=3, read_only=True)
    surname = serializers.CharField(max_length=255, min_length=6, read_only=True)
    tokens = serializers.JSONField(read_only=True)
    
    def get_tokens(self, obj):
        user = User.objects.get(email=obj['email'])

        return {
            "refresh": user.tokens()['refresh'],
            "access": user.tokens()['access']
        }

    class Meta:
        model = User
        fields=['id', 'email', 'password', 'username', 'name', 'surname', 'tokens']

    def validate(self, attrs):
        email = attrs.get('email', '')
        password = attrs.get('password', '')

        user = auth.authenticate(email=email, password=password)

        if not user:
            raise AuthenticationFailed('Invalid creditials, try again')

        if not user.is_active:
            raise AuthenticationFailed('Account disbled, contact admin')
        
        if not user.is_verified:
            raise AuthenticationFailed('Email is not verified')
                        
        return {
            'id': user.id,
            'email': user.email,
            'username': user.username,
            'name': user.name,
            'surname': user.surname,
            'tokens': user.tokens,
        }


class LogoutSerializer(serializers.Serializer):
    refresh = serializers.CharField()

    default_error_message = {
        'bad_token': ('Token is expired or invalid')
    }

    def validate(self, attrs):
        self.token = attrs['refresh']
        return attrs

    def save(self, **kwargs):

        try:
            RefreshToken(self.token).blacklist()

        except TokenError:
            self.fail('bad_token')


class ResetPasswordEmailRequestSerializer(serializers.Serializer):
    """
    Reset password by email serializer.

    """
    email = serializers.EmailField(min_length=2)

    class Meta:
        fields = ['email']

    def validate(self, attrs):
        email = attrs['data'].get('email', '')
        
        return super().validate(attrs)


class SetNewPasswordSerializer(serializers.Serializer):
    """
    Set new pasword serializer.
    
    """
    password = serializers.CharField(min_length=6, max_length=68, write_only=True)
    token = serializers.CharField(min_length=1, write_only=True)
    uidb64 = serializers.CharField(min_length=1, write_only=True)

    class Meta:
        fields=['password', 'token', 'uidb64']
    
    def validate(self, attrs):
        try:
            password = attrs.get('password', '')
            token = attrs.get('token', '')
            uidb64 = attrs.get('uidb64', '')
            id = force_str(urlsafe_base64_decode(uidb64))
            user = User.objects.get(id=id)

            if not PasswordResetTokenGenerator().check_token(user, token):
                raise AuthenticationFailed('The reset link is invalid', 401)
            user.set_password(password)
            user.save()

            return super().validate(attrs)
        except Exception as e:
            raise AuthenticationFailed('The reset link is invalid', 401)
        

class ChangeAccountSerializer(serializers.Serializer):
    name = serializers.CharField(min_length=6, max_length=68, write_only=True, allow_null=True)
    surname = serializers.CharField(min_length=6, max_length=68, write_only=True,  allow_null=True)
    favorite_sports = serializers.CharField(min_length=6, max_length=68, write_only=True, allow_null=True)
    birthday = serializers.DateField(allow_null=True)
    country = serializers.CharField(min_length=6, max_length=68, write_only=True, allow_null=True)
    locality = serializers.CharField(min_length=6, max_length=68, write_only=True,  allow_null=True)
    favorite_sports = serializers.PrimaryKeyRelatedField(queryset=Sport.objects.all(), many=True,  allow_null=True)

    class Meta:
        model = Event
        fields = ['name', 'surname', 'birthday', 'country', 'locality', 'favorite_sports']

    def update(self, instance, validated_data):
        if validated_data.get('name'):
            instance.name = validated_data.get('name', instance.name)
        if validated_data.get('surname', instance.surname):
            instance.surname = validated_data.get('surname', instance.surname)
        if validated_data.get('birthday', instance.birthday):
            instance.birthday = validated_data.get('birthday', instance.birthday)
        if validated_data.get('country', instance.country):
            instance.country = validated_data.get('country', instance.country)
        if validated_data.get('locality', instance.locality):
            instance.locality = validated_data.get('locality', instance.locality)
        if 'favorite_sports' in validated_data:
            instance.favorite_sports.clear()
            favorite_sports = validated_data.get('favorite_sports', instance.favorite_sports)
            for sport in favorite_sports:
                instance.favorite_sports.add(sport)

        instance.save()
        return instance


class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = ['id', 'email', 'username', 'name', 'surname', 'birthday', 'country', 'locality', 'favorite_sports']


class UploadPhotoSerializer(serializers.HyperlinkedModelSerializer):
    photo = serializers.ImageField()

    class Meta:
        model = User
        fields = ['id', 'email', 'photo']


    def update(self, instance, data, files):
        instance.photo = data.get('photo', instance.photo)
        instance.save()

        return instance



from django.conf import settings
from django.contrib.auth.tokens import PasswordResetTokenGenerator
from django.contrib.sites.shortcuts import get_current_site
from django.http import HttpResponsePermanentRedirect
from django.shortcuts import render
from django.utils.encoding import DjangoUnicodeDecodeError, force_str, smart_str, smart_bytes
from django.utils.http import urlsafe_base64_decode, urlsafe_base64_encode 
from django.urls import reverse
from events.permissions import IsOwnerOrReadOnly
from rest_framework import generics, permissions, status
from rest_framework.response import Response
from rest_framework_simplejwt.tokens import RefreshToken
from .serializers import (
    ChangeAccountSerializer, EmailVerificationSerializer, LoginSerializer, RegisterSerializer, 
    ResetPasswordEmailRequestSerializer, UploadPhotoSerializer, UserSerializer, SetNewPasswordSerializer
)
from .models import User
from .utils import Util
import jwt


class CustomRedirect(HttpResponsePermanentRedirect):

    allowed_schemes = ['sportevent']


class RegisterView(generics.GenericAPIView):
    serializer_class = RegisterSerializer

    def post(self, request):
        user = request.data
        serializer = self.serializer_class(data=user)
        serializer.is_valid(raise_exception=True)
        serializer.save()
        user_data = serializer.data
        user = User.objects.get(email=user_data['email'])
        token = RefreshToken.for_user(user).access_token
        current_site = get_current_site(request).domain
        relative_link = reverse('email-verify')
        
        absurl='http://' + current_site + relative_link + "?token=" + str(token) + "&redirect_url=" + "sportevent://successful_registry" 
        email_body= 'Hi, ' + user.username + '. Use link below to verify your email \n' + absurl
        data={'email_body': email_body, 'to_email': user.email, 'email_subject': 'Verify your email'}
        Util.send_email(data)
        return Response(user_data, status=status.HTTP_201_CREATED)


class VerifyEmail(generics.GenericAPIView):
    serializer_class = EmailVerificationSerializer

    def get(self, request):
        token = request.GET.get('token')
        redirect_url = request.GET.get('redirect_url')
        try:
            payload = jwt.decode(token, settings.SECRET_KEY, algorithms=['HS256'])
            user = User.objects.get(id=payload['user_id'])
            if not user.is_verified:
                user.is_verified = True
                user.save()
            return CustomRedirect(redirect_url)
        except jwt.ExpiredSignatureError as identifier:
            return Response({'error': 'Activation Expired'}, status=status.HTTP_400_BAD_REQUEST)
        except jwt.exceptions.DecodeError as identifier:
            return Response({'error': 'Invalid token'}, status=status.HTTP_400_BAD_REQUEST)


class LoginAPIView(generics.GenericAPIView):
    serializer_class = LoginSerializer

    def post(self, request):
        serializer = self.serializer_class(data=request.data)
        serializer.is_valid(raise_exception=True)

        return Response(serializer.data, status=status.HTTP_200_OK)


class LogoutAPIView(generics.GenericAPIView):
    permission_classes = (permissions.IsAuthenticated)

    def post(self, request):
        try:
            refresh_token = request.data['refresh_token']
            token = RefreshToken(refresh_token)
            token.blacklist()
            return Response(status=status.HTTP_205_RESET_CONTENT)
        except Exception as e:
            return Response(status=status.HTTP_400_BAD_REQUEST)


class RequestPassworResetEmail(generics.GenericAPIView):
    serializer_class = ResetPasswordEmailRequestSerializer

    def post(self, request):
        serializer = self.serializer_class(data=request.data)
        email = request.data.get('email', '')
        if User.objects.filter(email=email).exists():
            user = User.objects.get(email=email)
            uidb64 = urlsafe_base64_encode(smart_bytes(user.id))
            token = PasswordResetTokenGenerator().make_token(user)
            current_site = get_current_site(
                request=request).domain
            relative_link = reverse('password-reset-confirm', kwargs={'uidb64': uidb64, 'token': token})
            absurl='http://' + current_site + relative_link
            email_body= 'Hello, \n! Use link below to reset your password \n' + absurl
            data={'email_body': email_body, 'to_email': user.email, 'email_subject': 'Reset your password'}
            Util.send_email(data)

        return Response({'succsess': 'We have to sent you a link to reset your password'},
                        status=status.HTTP_200_OK)


class PasswordTokenCheckAPIView(generics.GenericAPIView):
    serializer_class = SetNewPasswordSerializer

    def get(self, request, uidb64, token):
        try:
            id=smart_str(urlsafe_base64_decode(uidb64))
            user=User.objects.get(id=id)

            if not PasswordResetTokenGenerator().check_token(user, token):
                return Response({'error': 'Token is not valid, pleace request a new one'})

            return Response({'success': True,
                             'message': 'Credentials Valid',
                             'uidb64':uidb64,
                             'token': token},
                             status=status.HTTP_200_OK)

        except DjangoUnicodeDecodeError as identifier:
            return Response({'error': 'Token is not valid, pleace request a new one'})


class SetNewPasswordAPIView(generics.GenericAPIView):
    serializer_class = SetNewPasswordSerializer

    def patch(self, request):
        serializer = self.serializer_class(data=request.data)
        serializer.is_valid(raise_exception=True)
        return Response({'success': True,
                         'message': 'Password reset success'},
                          status=status.HTTP_200_OK)


class ChangeAccountInformation(generics.UpdateAPIView):
    serializer_class = ChangeAccountSerializer
    permission_classes = [permissions.IsAuthenticated]
    queryset = User.objects.all()
    lookup_field = 'pk'
    
    def update(self, request, *args, **kwargs):
        instance = self.get_object()

        serializer = self.get_serializer(instance, data=request.data, partial=True)
        if serializer.is_valid(raise_exception=True):
            serializer.save()
        
            return Response({'success': True,
                            'message': 'User data updeting success'},
                            status=status.HTTP_200_OK)

        return Response({'success': False,
                         'message': 'User data updating FAILED'},
                         status=status.HTTP_400_BAD_REQUEST)


class GetUsersView(generics.ListAPIView):
    queryset = User.objects.all()
    serializer_class = UserSerializer
    permission_classes = [IsOwnerOrReadOnly]


class GetUserByIdView(generics.RetrieveUpdateDestroyAPIView):
    queryset = User.objects.all()
    serializer_class = UserSerializer
    permission_classes = [permissions.IsAuthenticated]


class UploadPhoto(generics.UpdateAPIView):
    serializer_class = UploadPhotoSerializer
    permission_classes = [permissions.IsAuthenticated]
    queryset = User.objects.all()
    lookup_field = 'pk'

    def update(self, request, *args, **kwargs):
        instance = self.get_object()
        instance.photo = request.FILES['photo']
        instance.save()
        
        return Response({'success': True,
                        'message': 'User photo uploading is success'},
                        status=status.HTTP_200_OK)

        # return Response({'success': False,
        #                  'message': 'User photo uploading FAILED'},
        #                  status=status.HTTP_400_BAD_REQUEST)

from django.contrib.auth.models import User
from rest_framework import serializers

class UserSerializer(serializers.ModelSerializer):
    # event = serializers.PrimaryKeyRelatedField(many=True, queryset=Event.objects.all())
    class Meta:
        model = User
        fields = ['id', 'username', 'password']
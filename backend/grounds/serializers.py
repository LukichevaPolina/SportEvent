from rest_framework import serializers
from grounds.models import Ground

class GroundSerializer(serializers.ModelSerializer):
    class Meta:
        model = Ground
        fields = ['id', 'name', 'sports', 'latitude', 'longitude', 'address']

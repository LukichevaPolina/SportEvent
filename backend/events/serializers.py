from dataclasses import field
from datetime import datetime
from msilib.schema import Error
from accounts.models import User
from django.core.exceptions import ValidationError
from rest_framework import serializers
from events.models import Event

class EventSerializer(serializers.ModelSerializer):
    owner = serializers.ReadOnlyField(source='owner.username')

    class Meta:
        model = Event
        fields = ['id', 'owner', 'sport', 'date', 'start_time', 'end_time', 'person_number', 'level', 'latitude', 'longitude', 'members']

    def validate(self, data):
        """
        Check that data is correct.
        """
        
        if all(fields in data for fields in ['end_time', 'start_time', 'date']):
            # Check that end_time after start_time
            if data['end_time'] < data['start_time']:
                raise ValidationError({"end_time": "End date must be after start date."})

            # Check that start_time after now
            now = datetime.now()
            if data['date'] == now.date() and data['start_time'] <= now.time():
                raise ValidationError({"start_time": "Start time must be after now."})

        return data


class EventJoinSerializer(serializers.ModelSerializer):
    """
    Serializer for joining member to event.

    """
    class Meta:
        model = Event
        fields = ['id', 'owner', 'sport', 'date', 'start_time', 'end_time', 'person_number', 'level', 'latitude', 'longitude', 'members']

   
    def update(self, instance, data):
        instance.members.add(data['members'][0])
        instance.save()
        return instance


class EventUnjoinSerializer(serializers.ModelSerializer):
    """
    Serializer for unjoining member to event.
    
    """
    class Meta:
        model = Event
        fields = ['id', 'owner', 'sport', 'date', 'start_time', 'end_time', 'person_number', 'level', 'latitude', 'longitude', 'members']

    def update(self, instance, data):
        instance.members.remove(data['members'][0])
        instance.save()
        return instance
        
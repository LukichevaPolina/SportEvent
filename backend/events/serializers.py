from dataclasses import field
from datetime import datetime
from msilib.schema import Error
from accounts.models import User
from django.core.exceptions import ValidationError
from rest_framework import serializers
from events.models import Event

class EventSerializer(serializers.ModelSerializer):
    owner = serializers.ReadOnlyField(source='owner.username')
    # free_seats = serializers.ReadOnlyField(source='person_number')

    class Meta:
        model = Event
        fields = ['id', 'owner', 'sport', 'date', 'start_time', 'end_time', 'person_number', 'free_seats', 'level', 'latitude', 'longitude', 'members']

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
        fields = ['id', 'owner', 'sport', 'date', 'start_time', 'end_time', 'person_number', 'free_seats', 'level', 'latitude', 'longitude', 'members']

   
    def update(self, instance, data):
        new_member = data['members'][0]
        if not new_member in instance.members.all():
            instance.members.add(new_member)
            instance.free_seats = instance.free_seats - 1
            instance.save()
        return instance


class EventUnjoinSerializer(serializers.ModelSerializer):
    """
    Serializer for unjoining member to event.
    
    """
    class Meta:
        model = Event
        fields = ['id', 'owner', 'sport', 'date', 'start_time', 'end_time', 'person_number', 'free_seats', 'level', 'latitude', 'longitude', 'members']

    def update(self, instance, data):
        member = data['members'][0]
        if member in instance.members.all():
            instance.members.remove(data['members'][0])
            instance.free_seats = instance.free_seats + 1
            instance.save()
        return instance
        
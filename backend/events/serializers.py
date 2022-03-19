from dataclasses import field
from datetime import datetime
from django.contrib.auth.models import User
from django.core.exceptions import ValidationError
from rest_framework import serializers
from events.models import Event

class EventSerializer(serializers.ModelSerializer):
    owner = serializers.ReadOnlyField(source='owner.username')
    class Meta:
        model = Event
        fields = ['id', 'owner', 'sport', 'date', 'start_time', 'end_time', 'person_number', 'level', 'latitude', 'longitude']

    def validate(self, data):
        """
        Check that data is correct.
        """

        # Check that end_time after start_time
        if data['end_time'] < data['start_time']:
            raise ValidationError({"end_time": "End date must be after start date."})

        # Check that start_time after now
        now = datetime.now()
        if data['date'] == now.date() and data['start_time'] <= now.time():
            raise ValidationError({"start_time": "Start time must be after now."})

        return data

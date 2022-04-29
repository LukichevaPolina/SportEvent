from django.urls import reverse
from events.models import Event
from accounts.models import User
from sports.models import Sport
from events.serializers import EventSerializer
from sports.serializers import SportSerializer
from rest_framework.test import APITestCase
from rest_framework import status
import json

class EventApiTestCase(APITestCase):
    def test_sport(self):
        # user = User.objects.create(email='test@email.com',
        #                            username='test_user',
        #                            password='password',
        #                            name='user_name',
        #                            surname='user_surname',
        #                            birthday='2000-01-01',
        #                            country='country',
        #                            locality='locality')
        sport = Sport.objects.create(sport='football')
        # event_1 = Event.objects.create(owner=user,
        #                                sport=sport, 
        #                                date='2022-05-01',
        #                                start_time='14:00',
        #                                end_time='16:00',
        #                                person_number=6,
        #                                level=1,
        #                                latitude=11,
        #                                longitude=11)
        # url = reverse('events')
        url = reverse('sports')
        response = self.client.get(url)
        sport_data = SportSerializer(sport).data

        self.assertEqual(status.HTTP_200_OK, response.status_code)
        self.assertEqual(sport_data, json.loads(response.content))
from django.urls import reverse
from events.models import Event
from accounts.models import User
from sports.models import Sport
from events.serializers import EventSerializer
from sports.serializers import SportSerializer
from rest_framework.test import APITestCase
from rest_framework import status
import json
import sportevent.settings
from django.core.mail import outbox


class EventApiTestCase(APITestCase):        
    def registration(self):
        with self.settings(EMAIL_BACKEND = 'django.core.mail.backends.locmem.EmailBackend'):
            response = self.client.post('/auth/register/', {"email": "test1@email.com",
                                                            "username": "username",
                                                            "password": "password",
                                                            "name": "user_name",
                                                            "surname": "user_surname",
                                                            "birthday": "2000-01-01",
                                                            "country":"country",
                                                            "locality": "locality",
                                                            "favorite_sports": [1]})
            print(response.data)
            print(outbox)

    def test_sport(self):
        sport = Sport.objects.create(sport='football')
        self.registration()
        password = 'password'
        user = User.objects.create(email='test@email.com',
                                   username='test_user',
                                   password=password,
                                   name='user_name',
                                   surname='user_surname',
                                   birthday='2000-01-01',
                                   country='country',
                                   locality='locality')
        event_1 = Event.objects.create(owner=user,
                                       sport=sport, 
                                       date='2022-05-01',
                                       start_time='14:00',
                                       end_time='16:00',
                                       person_number=6,
                                       level=1,
                                       latitude=11,
                                       longitude=11)
        login = self.client.post('/auth/login/', {'email': user.email, 'password':password})
        # response = self.client.get('/events/1/', format='json', headers={'Authorization':})
        # print(response.content)

        # event_data = EventSerializer(event_1).data

        self.assertEqual(status.HTTP_200_OK, response.status_code)
        self.assertEqual(event_data, json.loads(response.content))
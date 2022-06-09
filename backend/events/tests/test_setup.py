from rest_framework.test import APITestCase, APIClient
from django.urls import reverse
from accounts.models import User
import datetime

class TestSetUp(APITestCase):

    def setUp(self):
        self.register_url = reverse('register')
        self.login_url = reverse('login')
        self.sports_url = reverse('sports')
        self.events_url = reverse('events')

        self.user_data = {
            'email': 'email@gmail.com',
            'username': 'username',
            'password': 'password',
            'name': 'name',
            'surname': 'surname',
            'birthday': '2001-03-15',
            'country': 'country',
            'locality': 'locality'
        }

        self.sport_data = {
            'sport': 'football'
        }

        self.event_data = {
            "sport": 1,
            "date": str(datetime.date.today() + datetime.timedelta(1)),
            "start_time": '9:00',
            "end_time": '10:00',
            "person_number": 10,
            "free_seats": 10,
            "level": 1,
            "latitude": 10.0,
            "longitude": 10.0,
            "members": [],
            "address": "Addres"

        }

        self.filter = {
            'sport': 1
        }

        return super().setUp()

    def tearDown(self):
        return super().tearDown()

    def register_and_verify(self, user_data):
        register_response = self.client.post(
            self.register_url, user_data, format='json')
        email = register_response.json()['email']
        user = User.objects.get(email=email)
        user.is_verified = True
        user.save()

    def register_verify_login(self, user_data):
        self.register_and_verify(user_data)
        res = self.client.post(
            self.login_url, user_data, format="json")
        return res.json()['tokens']['access']

    def create_sport(self):
        res = self.client.post(
            self.sports_url, self.sport_data, format="json")
        return res.json()['sport']
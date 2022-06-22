from rest_framework.test import APITestCase
from django.urls import reverse
from accounts.models import User

class TestSetUp(APITestCase):

    def setUp(self):
        self.sports_url = reverse('sports')

        self.sport_data = {
            'sport': 'football'
        }

        return super().setUp()

    def tearDown(self):
        return super().tearDown()
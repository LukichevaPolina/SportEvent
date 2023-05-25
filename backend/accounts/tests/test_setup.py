from rest_framework.test import APITestCase, APIClient
from django.urls import reverse
from ..models import User

class TestSetUp(APITestCase):

    def setUp(self):
        self.register_url = reverse('register')
        self.login_url = reverse('login')
        self.change_info = '/auth/change-account-info/'
        self.users = '/auth/users/'

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

        self.new_user_data = {
            'name': 'newName',
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

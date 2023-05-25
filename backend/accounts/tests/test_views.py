from .test_setup import TestSetUp
from ..models import User
import copy

class TestViews(TestSetUp):
    def test_uesr_register_correct_data(self):
        res = self.client.post(
            self.register_url, self.user_data, format="json")
        self.assertEqual(res.json()['email'], self.user_data['email'])
        self.assertEqual(res.json()['username'], self.user_data['username'])
        self.assertEqual(res.json()['name'], self.user_data['name'])
        self.assertEqual(res.json()['surname'], self.user_data['surname'])
        self.assertEqual(res.json()['birthday'], self.user_data['birthday'])
        self.assertEqual(res.json()['country'], self.user_data['country'])
        self.assertEqual(res.json()['locality'], self.user_data['locality'])
        self.assertEqual(res.status_code, 201)

    def test_user_register_no_data(self):
        res = self.client.post(self.register_url)

        self.assertEqual(res.status_code, 400)

    def test_unverified_email_login(self):
        self.client.post(
            self.register_url, self.user_data, format="json")
        res = self.client.post(
            self.login_url, self.user_data, format="json")

        self.assertEqual(res.status_code, 401)

    def test_verified_email_login(self):
        self.register_and_verify(self.user_data)
        res = self.client.post(
            self.login_url, self.user_data, format="json")
        self.assertEqual(res.json()['email'], self.user_data['email'])
        self.assertEqual(res.json()['username'], self.user_data['username'])
        self.assertEqual(res.json()['name'], self.user_data['name'])
        self.assertEqual(res.json()['surname'], self.user_data['surname'])
        self.assertEqual(res.status_code, 200)

    def test_update_user_info(self):
        self.register_and_verify(self.user_data)
        res = self.client.post(
            self.login_url, self.user_data, format="json")
        access_token = res.json()['tokens']['access']
        id =  str(res.json()['id'])
        res = self.client.patch(self.change_info+id+"/", self.new_user_data, format="json", HTTP_AUTHORIZATION='Bearer {}'.format(access_token))
        user = User.objects.get(email=self.user_data['email'])
        
        self.assertEqual(res.json()['success'], True)
        self.assertEqual(user.name, self.new_user_data['name'])
        self.assertEqual(user.surname, self.user_data['surname'])
        self.assertEqual(res.status_code, 200)

    def test_get_user(self):
        self.register_and_verify(self.user_data)
        res = self.client.post(
            self.login_url, self.user_data, format="json")
        access_token = res.json()['tokens']['access']
        id =  str(res.json()['id'])
        res = self.client.get(self.users+id+"/", format="json", HTTP_AUTHORIZATION='Bearer {}'.format(access_token))
                
        self.assertEqual(res.json()['email'], self.user_data['email'])
        self.assertEqual(res.json()['username'], self.user_data['username'])
        self.assertEqual(res.json()['name'], self.user_data['name'])
        self.assertEqual(res.json()['surname'], self.user_data['surname'])
        self.assertEqual(res.status_code, 200)

    def test_get_users(self):
        self.register_and_verify(self.user_data)
        new_user = copy.deepcopy(self.user_data)
        new_user['email'] = 'newemail@gmail.com'
        new_user['username'] = 'newusername'
        self.register_and_verify(new_user)
        res = self.client.post(
            self.login_url, self.user_data, format="json")
        access_token = res.json()['tokens']['access']
        res = self.client.get(self.users, format="json", HTTP_AUTHORIZATION='Bearer {}'.format(access_token))
        user1 = dict(res.data[0])
        user2 = dict(res.data[1])
        
        self.assertEqual(user1['email'], self.user_data['email'])
        self.assertEqual(user1['username'], self.user_data['username'])
        self.assertEqual(user1['name'], self.user_data['name'])
        self.assertEqual(user1['surname'], self.user_data['surname'])
        self.assertEqual(user2['email'], new_user['email'])
        self.assertEqual(user2['username'], new_user['username'])
        self.assertEqual(user2['name'], new_user['name'])
        self.assertEqual(user2['surname'], new_user['surname'])
        self.assertEqual(res.status_code, 200)
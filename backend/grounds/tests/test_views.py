from .test_setup import TestSetUp
from accounts.models import User
import copy
import datetime

class TestGroundViews(TestSetUp):
    def test_create_ground(self):
        access_token = self.register_verify_login(self.user_data)
        self.create_sport()
        res = self.client.post(self.grounds_url, self.ground_data,
                               format="json", HTTP_AUTHORIZATION='Bearer {}'.format(access_token))

        self.assertEqual(res.json()['sports'], self.ground_data['sports'])
        self.assertEqual(res.json()['address'], self.ground_data['address'])
        self.assertEqual(res.json()['name'], self.ground_data['name'])
        self.assertEqual(res.json()['latitude'], self.ground_data['latitude'])
        self.assertEqual(res.json()['longitude'], self.ground_data['longitude'])
        self.assertEqual(res.status_code, 201)

    def test_get_ground(self):
        access_token = self.register_verify_login(self.user_data)
        self.create_sport()
        res = self.client.post(self.grounds_url, self.ground_data,
                               format="json", HTTP_AUTHORIZATION='Bearer {}'.format(access_token))
        ground_id = str(res.json()['id'])
        res = self.client.get(self.grounds_url+'{}/'.format(ground_id), format="json", HTTP_AUTHORIZATION='Bearer {}'.format(access_token))

        self.assertEqual(res.json()['sports'], self.ground_data['sports'])
        self.assertEqual(res.json()['address'], self.ground_data['address'])
        self.assertEqual(res.json()['name'], self.ground_data['name'])
        self.assertEqual(res.json()['latitude'], self.ground_data['latitude'])
        self.assertEqual(res.json()['longitude'], self.ground_data['longitude'])
        self.assertEqual(res.status_code, 200)

    def test_get_events(self):
        access_token = self.register_verify_login(self.user_data)
        self.create_sport()
        self.client.post(self.grounds_url, self.ground_data,
                               format="json", HTTP_AUTHORIZATION='Bearer {}'.format(access_token))
        self.client.post(self.grounds_url, self.ground_data,
                               format="json", HTTP_AUTHORIZATION='Bearer {}'.format(access_token))
        res = self.client.get(self.grounds_url, format="json", HTTP_AUTHORIZATION='Bearer {}'.format(access_token))

        for ground in res.data:
            ground = dict(ground)
            self.assertEqual(ground['sports'], self.ground_data['sports'])
            self.assertEqual(ground['address'], self.ground_data['address'])
            self.assertEqual(ground['name'], self.ground_data['name'])
            self.assertEqual(ground['latitude'], self.ground_data['latitude'])
            self.assertEqual(ground['longitude'], self.ground_data['longitude'])

        self.assertEqual(res.status_code, 200)
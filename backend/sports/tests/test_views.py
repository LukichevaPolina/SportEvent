from .test_setup import TestSetUp
from accounts.models import User
import copy
import datetime

class TestGroundViews(TestSetUp):
    def test_create_sport(self):
        res = self.client.post(self.sports_url, self.sport_data, format="json")

        self.assertEqual(res.json()['sport'], self.sport_data['sport'])
        self.assertEqual(res.status_code, 201)

    def test_get_sport(self):
        res = self.client.post(self.sports_url, self.sport_data, format="json")
        sport_id = str(res.json()['id'])
        res = self.client.get(self.sports_url+'{}/'.format(sport_id), format="json")

        self.assertEqual(res.json()['sport'], self.sport_data['sport'])
        self.assertEqual(res.status_code, 200)

    def test_get_sports(self):
        self.client.post(self.sports_url, self.sport_data, format="json")
        self.client.post(self.sports_url, self.sport_data, format="json")
        res = self.client.get(self.sports_url, format="json")

        for sport in res.data:
            sport = dict(sport)
            self.assertEqual(sport['sport'], self.sport_data['sport'])

        self.assertEqual(res.status_code, 200)
from .test_setup import TestSetUp
from accounts.models import User
import copy
import datetime


class TestEventViews(TestSetUp):
    def test_create_event(self):
        access_token = self.register_verify_login(self.user_data)
        sport = self.create_sport()
        res = self.client.post(self.events_url, self.event_data,
                               format="json", HTTP_AUTHORIZATION='Bearer {}'.format(access_token))

        self.assertEqual(res.json()['sport'], sport)
        self.assertEqual(res.json()['date'], self.event_data['date'])
        self.assertEqual(res.status_code, 201)

    def test_get_event(self):
        access_token = self.register_verify_login(self.user_data)
        sport = self.create_sport()
        res = self.client.post(self.events_url, self.event_data,
                               format="json", HTTP_AUTHORIZATION='Bearer {}'.format(access_token))
        event_id = str(res.json()['id'])
        res = self.client.get(self.events_url+'{}/'.format(event_id), format="json", HTTP_AUTHORIZATION='Bearer {}'.format(access_token))

        self.assertEqual(res.json()['sport'], sport)
        self.assertEqual(res.json()['date'], self.event_data['date'])
        self.assertEqual(res.status_code, 200)

    def test_get_events(self):
        access_token = self.register_verify_login(self.user_data)
        sport = self.create_sport()
        self.client.post(self.events_url, self.event_data,
                               format="json", HTTP_AUTHORIZATION='Bearer {}'.format(access_token))
        self.client.post(self.events_url, self.event_data,
                               format="json", HTTP_AUTHORIZATION='Bearer {}'.format(access_token))
        res = self.client.get(self.events_url, format="json", HTTP_AUTHORIZATION='Bearer {}'.format(access_token))

        for event in res.data:
            event = dict(event)
            self.assertEqual(event['sport'], sport)
            self.assertEqual(event['sport'], sport)
            self.assertEqual(event['date'], self.event_data['date'])

        self.assertEqual(res.status_code, 200)
        

    def test_get_date_events(self):
        access_token = self.register_verify_login(self.user_data)
        self.create_sport()
        self.client.post(self.events_url, self.event_data,
                               format="json", HTTP_AUTHORIZATION='Bearer {}'.format(access_token))

        date =  str(datetime.date.today() + datetime.timedelta(2))
        new_event = copy.deepcopy(self.event_data)
        new_event['date'] = date

        self.client.post(self.events_url, new_event,
                               format="json", HTTP_AUTHORIZATION='Bearer {}'.format(access_token))

        res = self.client.get(self.events_url+'date/?date={}'.format(date), new_event,
                               format="json", HTTP_AUTHORIZATION='Bearer {}'.format(access_token))

        for event in res.data:
            event = dict(event)
            self.assertEqual(event['date'], date)

        self.assertEqual(res.status_code, 200)

    
    def test_get_schedule_events(self):
        access_token = self.register_verify_login(self.user_data)
        self.create_sport()
        self.client.post(self.events_url, self.event_data,
                               format="json", HTTP_AUTHORIZATION='Bearer {}'.format(access_token))

        date =  str(datetime.date.today() + datetime.timedelta(2))
        new_event = copy.deepcopy(self.event_data)
        new_event['date'] = date

        self.client.post(self.events_url, new_event,
                               format="json", HTTP_AUTHORIZATION='Bearer {}'.format(access_token))

        res = self.client.get(self.events_url+'schedule/?date={}'.format(date), new_event,
                               format="json", HTTP_AUTHORIZATION='Bearer {}'.format(access_token))

        for event in res.data:
            event = dict(event)
            self.assertEqual(event['date'], date)

        self.assertEqual(res.status_code, 200)


    def test_get_created_events(self):
        access_token = self.register_verify_login(self.user_data)
        self.create_sport()
        self.client.post(self.events_url, self.event_data,
                               format="json", HTTP_AUTHORIZATION='Bearer {}'.format(access_token))

        username = User.objects.get(email=self.user_data['email']).username

        date =  str(datetime.date.today() + datetime.timedelta(2))
        new_event = copy.deepcopy(self.event_data)
        new_event['date'] = date

        self.client.post(self.events_url, new_event,
                               format="json", HTTP_AUTHORIZATION='Bearer {}'.format(access_token))

        res = self.client.get(self.events_url+'created/', new_event,
                               format="json", HTTP_AUTHORIZATION='Bearer {}'.format(access_token))

        for event in res.data:
            event = dict(event)
            self.assertEqual(event['owner'], username)

        self.assertEqual(res.status_code, 200)

    def test_get_visited_events(self):
        access_token = self.register_verify_login(self.user_data)
        self.create_sport()
        self.client.post(self.events_url, self.event_data,
                               format="json", HTTP_AUTHORIZATION='Bearer {}'.format(access_token))

        user_id = User.objects.get(email=self.user_data['email']).id

        date =  str(datetime.date.today() + datetime.timedelta(2))
        new_event = copy.deepcopy(self.event_data)
        new_event['date'] = date

        self.client.post(self.events_url, new_event,
                               format="json", HTTP_AUTHORIZATION='Bearer {}'.format(access_token))

        new_user = copy.deepcopy(self.user_data)
        new_user['email'] = 'newemail@gmail.com'
        new_user['username'] = 'newusername'
        new_access_token = self.register_verify_login(new_user)
        user = User.objects.get(email=new_user['email'])
        res = self.client.patch('/events/1/join/', {}, format="json", HTTP_AUTHORIZATION='Bearer {}'.format(new_access_token))
 
        res = self.client.get(self.events_url+'visited/', new_event,
                               format="json", HTTP_AUTHORIZATION='Bearer {}'.format(new_access_token))

        for event in res.data:
            event = dict(event)
            self.assertTrue(user.id in event['members'])

        self.assertEqual(res.status_code, 200)

    
    def test_join_events(self):
        access_token = self.register_verify_login(self.user_data)
        self.create_sport()
        res = self.client.post(self.events_url, self.event_data,
                               format="json", HTTP_AUTHORIZATION='Bearer {}'.format(access_token))
        event_id = str(res.json()['id'])
        new_user = copy.deepcopy(self.user_data)
        new_user['email'] = 'newemail@gmail.com'
        new_user['username'] = 'newusername'
        new_access_token = self.register_verify_login(new_user)
        user = User.objects.get(email=new_user['email'])
        res = self.client.patch('/events/1/join/', {}, format="json", HTTP_AUTHORIZATION='Bearer {}'.format(new_access_token))

        
        self.assertTrue(user.id in res.json()['members'])

   
    def test_unjoin_events(self):
        access_token = self.register_verify_login(self.user_data)
        self.create_sport()
        res = self.client.post(self.events_url, self.event_data,
                               format="json", HTTP_AUTHORIZATION='Bearer {}'.format(access_token))
        event_id = str(res.json()['id'])
        new_user = copy.deepcopy(self.user_data)
        new_user['email'] = 'newemail@gmail.com'
        new_user['username'] = 'newusername'
        new_access_token = self.register_verify_login(new_user)

        user = User.objects.get(email=new_user['email'])
        res = self.client.patch('/events/1/join/', {}, format="json", HTTP_AUTHORIZATION='Bearer {}'.format(new_access_token))
        res = self.client.patch('/events/1/unjoin/', {}, format="json", HTTP_AUTHORIZATION='Bearer {}'.format(new_access_token))

        
        self.assertFalse(user.id in res.json()['members'])

    def test_get_filtered_events(self):
        access_token = self.register_verify_login(self.user_data)
        sport = self.create_sport()
        self.client.post(self.events_url, self.event_data,
                               format="json", HTTP_AUTHORIZATION='Bearer {}'.format(access_token))

        username = User.objects.get(email=self.user_data['email']).username

        date =  str(datetime.date.today() + datetime.timedelta(2))
        new_event = copy.deepcopy(self.event_data)
        new_event['date'] = date

        self.client.post(self.events_url, new_event,
                               format="json", HTTP_AUTHORIZATION='Bearer {}'.format(access_token))

        res = self.client.get(self.events_url+'filters/', self.filter,
                               format="json", HTTP_AUTHORIZATION='Bearer {}'.format(access_token))

        for event in res.data:
            event = dict(event)
            self.assertEqual(event['sport'], sport)

        self.assertEqual(res.status_code, 200)
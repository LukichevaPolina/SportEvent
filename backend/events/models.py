from django.core.validators import MaxValueValidator, MinValueValidator
from django.core.exceptions import ValidationError
from datetime import datetime
from dateutil.relativedelta import relativedelta
from django.db import models

SPORT = ((1, 'football'),
         (2, 'bike'),
         (3, 'running'),
         (4, 'pilates'))

LEVEL = ((1, 'None'),
         (2, 'Low'),
         (3, 'Medium'),
         (4, 'Hard'))

class Event(models.Model):
    # owner = models.ForeignKey('accounts.User', related_name='created_events', on_delete=models.CASCADE)
    sport = models.CharField(choices=SPORT, default='running', max_length=100)

    #TODO: think about replace date validation to events/serializers.py
    date_today = datetime.now().date()
    date_next_month = date_today + relativedelta(months=+1)

    date = models.DateField(validators=[MinValueValidator(date_today), MaxValueValidator(date_next_month)])
    start_time = models.TimeField()
    end_time = models.TimeField()
    person_number = models.IntegerField(validators=[MinValueValidator(2), MaxValueValidator(15)], default=5)
    level = models.CharField(choices=LEVEL, default='None', max_length=100)
    latitude = models.FloatField(validators=[MinValueValidator(-90), MaxValueValidator(90)])
    longitude = models.FloatField(validators=[MinValueValidator(-180), MaxValueValidator(180)])
    
    # members

    class Meta:
        ordering = ['date']
       
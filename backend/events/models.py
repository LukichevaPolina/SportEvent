from django.core.validators import MaxValueValidator, MinValueValidator
from datetime import datetime
from dateutil.relativedelta import relativedelta
from django.db import models

LEVEL = ((1, 'None'),
         (2, 'Low'),
         (3, 'Medium'),
         (4, 'Hard'))

class Event(models.Model):
    owner = models.ForeignKey('accounts.User', related_name='owner', on_delete=models.CASCADE)
    sport = models.ForeignKey('sports.Sport', on_delete=models.CASCADE)

    date_today = datetime.now().date()
    date_next_month = date_today + relativedelta(months=+1)

    date = models.DateField(validators=[MinValueValidator(date_today), MaxValueValidator(date_next_month)])
    start_time = models.TimeField()
    end_time = models.TimeField()
    person_number = models.IntegerField(validators=[MinValueValidator(0), MaxValueValidator(15)], default=5)
    free_seats = models.IntegerField(validators=[MinValueValidator(0), MaxValueValidator(15)], default=5)
    level = models.CharField(choices=LEVEL, default='None', max_length=100)
    latitude = models.FloatField(validators=[MinValueValidator(-90), MaxValueValidator(90)])
    longitude = models.FloatField(validators=[MinValueValidator(-180), MaxValueValidator(180)])
    address = models.CharField(max_length=255, default='', unique=False, db_index=True)
    
    members = models.ManyToManyField('accounts.User', related_name='members', blank=True)

    class Meta:
        ordering = ['date', 'start_time']
       
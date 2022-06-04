from django.db import models
from django.core.validators import MaxValueValidator, MinValueValidator

class Ground(models.Model):
    name = models.CharField(max_length=255, default='', db_index=True)
    sports = models.ManyToManyField('sports.Sport', related_name='sports')
    latitude = models.FloatField(validators=[MinValueValidator(-90), MaxValueValidator(90)])
    longitude = models.FloatField(validators=[MinValueValidator(-180), MaxValueValidator(180)])
    address = models.CharField(max_length=255, default='', db_index=True)
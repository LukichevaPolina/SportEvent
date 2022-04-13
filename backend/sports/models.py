from django.db import models

class Sport(models.Model):

    sport = models.CharField(max_length=255, default='', db_index=True)

    class Meta:
        ordering = ['sport']

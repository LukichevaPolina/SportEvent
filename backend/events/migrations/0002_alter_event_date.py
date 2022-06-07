# Generated by Django 3.2.5 on 2022-05-30 10:41

import datetime
import django.core.validators
from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('events', '0001_initial'),
    ]

    operations = [
        migrations.AlterField(
            model_name='event',
            name='date',
            field=models.DateField(validators=[django.core.validators.MinValueValidator(datetime.date(2022, 5, 30)), django.core.validators.MaxValueValidator(datetime.date(2022, 6, 30))]),
        ),
    ]
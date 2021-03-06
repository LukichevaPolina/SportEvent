# Generated by Django 4.0.4 on 2022-06-25 12:34

import datetime
from django.conf import settings
import django.core.validators
from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    initial = True

    dependencies = [
        ('sports', '0001_initial'),
        migrations.swappable_dependency(settings.AUTH_USER_MODEL),
    ]

    operations = [
        migrations.CreateModel(
            name='Event',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('date', models.DateField(validators=[django.core.validators.MinValueValidator(datetime.date(2022, 6, 24)), django.core.validators.MaxValueValidator(datetime.date(2022, 7, 24))])),
                ('start_time', models.TimeField()),
                ('end_time', models.TimeField()),
                ('person_number', models.IntegerField(default=5, validators=[django.core.validators.MinValueValidator(0), django.core.validators.MaxValueValidator(15)])),
                ('free_seats', models.IntegerField(default=5, validators=[django.core.validators.MinValueValidator(0), django.core.validators.MaxValueValidator(15)])),
                ('level', models.CharField(choices=[(1, 'None'), (2, 'Low'), (3, 'Medium'), (4, 'Hard')], default='None', max_length=100)),
                ('latitude', models.FloatField(validators=[django.core.validators.MinValueValidator(-90), django.core.validators.MaxValueValidator(90)])),
                ('longitude', models.FloatField(validators=[django.core.validators.MinValueValidator(-180), django.core.validators.MaxValueValidator(180)])),
                ('address', models.CharField(db_index=True, default='', max_length=255)),
                ('members', models.ManyToManyField(blank=True, related_name='members', to=settings.AUTH_USER_MODEL)),
                ('owner', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, related_name='owner', to=settings.AUTH_USER_MODEL)),
                ('sport', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='sports.sport')),
            ],
            options={
                'ordering': ['date', 'start_time'],
            },
        ),
    ]

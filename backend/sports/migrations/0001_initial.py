# Generated by Django 4.0.4 on 2022-04-30 12:38

from django.db import migrations, models


class Migration(migrations.Migration):

    initial = True

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='Sport',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('sport', models.CharField(db_index=True, default='', max_length=255)),
            ],
            options={
                'ordering': ['sport'],
            },
        ),
    ]

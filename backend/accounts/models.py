from django.contrib.auth.models import (AbstractBaseUser, BaseUserManager, PermissionsMixin)
from django.contrib.postgres.fields import ArrayField
from django.db import models
from rest_framework_simplejwt.tokens import RefreshToken

SEX = ((1, 'None'),
       (2, 'Male'),
       (3, 'Female'))

class UserManager(BaseUserManager):
    """
    Creates and saves a new User.

    """
    use_in_migrations = True

    def create_user(self, username, email, password=None, name=None, surname=None, birthday=None, country=None, locality=None, favorite_sports=None):
        if username is None:
            raise TypeError('User should have a username')
        if email is None:
            raise TypeError('User should have a email')
        
        user = self.model(username=username,
                          email=self.normalize_email(email),
                          name=name,
                          surname=surname,
                          birthday=birthday,
                          country=country,
                          locality=locality)
        user.set_password(password)
        user.save()
        if favorite_sports:
            for sports in favorite_sports:
                user.favorite_sports.add(sports)
        return user
    
    def create_superuser(self, username, email, password=None):
        if password is None:
            raise TypeError('Password should not be None')
        
        user = self.create_user(username, email, password)       
        user.is_superuser = True
        user.is_staff=True
        user.save()
        
        return user
    

class User(AbstractBaseUser, PermissionsMixin):
    username = models.CharField(max_length=255, unique=True, db_index=True)
    email = models.EmailField(max_length=255, unique=True, db_index=True)

    is_verified = models.BooleanField(default=False)
    is_active = models.BooleanField(default=True)
    is_staff = models.BooleanField(default=False)
    created_at = models.DateTimeField(auto_now_add=True)
    upadeted_at = models.DateTimeField(auto_now_add=True)

    name = models.CharField(max_length=255, default='', unique=False, db_index=True)
    surname = models.CharField(max_length=255, default='', unique=False, db_index=True)
    # sex = models.CharField(choices=SEX, default='None', max_length=100)
    favorite_sports = models.ManyToManyField('sports.Sport', related_name='favorite_sports', blank=True)
    birthday = models.DateField(null=True, unique=False)
    country = models.CharField(max_length=255, default='', unique=False)
    locality = models.CharField(max_length=255, default='', unique=False)
    photo = models.ImageField(upload_to='avatars', blank=True)

    USERNAME_FIELD = 'email'  # field that using as unique id
    REQUIRED_FIELDS = ['username',]  # fields that will be requested when creating a user

    objects = UserManager()

    def __str__(self):
        return self.username

    def tokens(self):
        """
        Tokens.

        """
        refresh = RefreshToken.for_user(self)
        return {
            "refresh": str(refresh),
            "access": str(refresh.access_token)
        }

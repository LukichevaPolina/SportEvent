from django.contrib.auth.models import (AbstractBaseUser, BaseUserManager, PermissionsMixin)
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

    def create_user(self, username, email, password=None):
        if username is None:
            raise TypeError('User should have a username')
        if email is None:
            raise TypeError('User should have a email')
        
        user = self.model(username=username, email=self.normalize_email(email))
        user.set_password(password)
        user.save()
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
    # auth_provider = models.CharField(
    #     max_length=255, blank=False,
    #     null=False, default=AUTH_PROVIDERS.get('email'))

    name = models.CharField(max_length=255, default='', unique=False, db_index=True)
    surname = models.CharField(max_length=255, default='', unique=False, db_index=True)
    sex = models.CharField(choices=SEX, default='None', max_length=100)

    # TODO: connect other database
    # TODO: add default value
    # favorite_sports = models.TextField()  # use arrays from other database

    # TODO: add default value
    # birthday = models.DateField()

    # TODO: learn how to use filds from other tables
    # created_events - use array from other database
    # joined_events - use array from other database

    # created_fields - use array from other database
    # ..._fields - use array from other database

    USERNAME_FIELD = 'email'  # field that using as unique id
    REQUIRED_FIELDS = ['username',]  # fields that will be requested when creating a user

    objects = UserManager()

    def __str__(self):
        return self.email

    def tokens(self):
        """
        Tokens.

        """
        refresh = RefreshToken.for_user(self)
        return {
            'refresh': str(refresh),
            'access': str(refresh.access_token)
        }
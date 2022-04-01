from django.contrib.auth.models import (AbstractBaseUser, BaseUserManager, PermissionsMixin)
from django.db import models


class UserManager(BaseUserManager):
    """
    Creates and saves a new User.

    """
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
        
        user=self.create_user(username, email, password)       
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
    upadeted_at = models.DateTimeField(auto_now_add=True)

    USERNAME_FIELD='email'  # field that using as unique id
    REQUIRED_FIELDS=['username',]  # fields that will be requested when creating a user

    objects = UserManager()

    def __str__(self):
        return self.email

    def tokens(self):
        """
        Tokens.

        """
        return ''
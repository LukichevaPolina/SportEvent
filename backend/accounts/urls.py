from django.urls import path
from rest_framework.urlpatterns import format_suffix_patterns
from .views import VerifyEmail, RegisterView

urlpatterns = [
    path('register/', RegisterView.as_view(), name='register'),
    path('email-verify/', VerifyEmail.as_view(), name='email_verify'),
]

urlpatterns = format_suffix_patterns(urlpatterns)
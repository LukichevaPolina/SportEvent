from django.urls import path
from rest_framework.urlpatterns import format_suffix_patterns
from rest_framework_simplejwt.views import TokenRefreshView
from .views import VerifyEmail, LoginAPIView, LogoutAPIView, PasswordTokenCheckAPIView, UploadPhoto, RegisterView, RequestPassworResetEmail, SetNewPasswordAPIView, ChangeAccountInformation, GetUsersView, GetUserByIdView



urlpatterns = [
    path('register/', RegisterView.as_view(), name='register'),
    path('login/', LoginAPIView.as_view(), name='login'),
    path('logout/', LogoutAPIView.as_view(), name='logout'),
    path('email-verify/', VerifyEmail.as_view(), name='email-verify'),
    path('token/refresh/', TokenRefreshView.as_view(), name='token_refresh'),
    path('request-reset-email', RequestPassworResetEmail.as_view(), name='request-reset-email'),
    path('password-reset/<uidb64>/<token>', PasswordTokenCheckAPIView.as_view(), name='password-reset-confirm'),
    path('password-reset-complete', SetNewPasswordAPIView.as_view(), name='password-reset-complete'),
    path('change-account-info/<int:pk>/', ChangeAccountInformation.as_view(), name='change-info-info'),
    path('users/<int:pk>/', GetUserByIdView.as_view()),
    path('users/', GetUsersView.as_view()),
    # path('get_photo/')
    path('upload_photo/<int:pk>/', UploadPhoto.as_view())
    # path('delete?photo)
]

urlpatterns = format_suffix_patterns(urlpatterns)

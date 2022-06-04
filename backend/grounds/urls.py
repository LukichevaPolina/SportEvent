from django.urls import path
from rest_framework.urlpatterns import format_suffix_patterns
from grounds import views

urlpatterns = [
    path('grounds/', views.GroundList.as_view(), name='grounds'),
    path('grounds/<int:pk>/', views.GroundDetail.as_view()),
]

urlpatterns = format_suffix_patterns(urlpatterns)
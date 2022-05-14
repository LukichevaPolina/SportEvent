from django.urls import path
from rest_framework.urlpatterns import format_suffix_patterns
from sports import views

urlpatterns = [
    path('sports/', views.SportList.as_view(), name='sports'),
    path('sports/<int:pk>/', views.SportDetail.as_view()),
]

urlpatterns = format_suffix_patterns(urlpatterns)
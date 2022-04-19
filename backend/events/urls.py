from django.urls import path
from rest_framework.urlpatterns import format_suffix_patterns
from events import views

urlpatterns = [
    path('events/', views.EventList.as_view()),
    path('events/<int:pk>/', views.EventDetail.as_view()),
    path('events/schedule/', views.EventSchedule.as_view()),
    path('events/<int:id>/join/', views.EventJoinAPIView.as_view()),
    path('events/<int:id>/unjoin/', views.EventUnjoinAPIView.as_view()),
    path('events/date/', views.EventDate.as_view()),
    path('events/after_date/', views.EventAfterDate.as_view()),
    path('events/visited/', views.EventVisited.as_view()),
    path('events/created/', views.EventCreated.as_view()),
    path('events/filters/', views.EventFilters.as_view())
]

urlpatterns = format_suffix_patterns(urlpatterns)
from django.contrib.auth.models import User
from django.db.models import Count
from django.db.models import Q
from requests import request
from events.models import Event
from events.permissions import IsEventMember, IsOwnerOrReadOnly
from events.serializers import EventSerializer, EventJoinSerializer, EventUnjoinSerializer, EventFilterSerializer
from rest_framework import generics, permissions, status
from rest_framework.response import Response
from datetime import datetime


class EventList(generics.ListCreateAPIView):
    """
    List of all events.

    """
    queryset = Event.objects.all()
    serializer_class = EventSerializer
    permission_classes = [permissions.IsAuthenticated]

    def perform_create(self, serializer):
        serializer.save(owner=self.request.user)


class EventDetail(generics.RetrieveUpdateDestroyAPIView):
    """
    Return certain event by id.
    
    """
    queryset = Event.objects.all()
    serializer_class = EventSerializer
    permission_classes = [permissions.IsAuthenticated]


class EventSchedule(generics.ListAPIView):
    """
    Return schedule for current user.

    """
    serializer_class = EventSerializer
    permission_classes = [permissions.IsAuthenticated]

    def get_queryset(self):
        """
        This view should return a list of all the events
        for the currently authenticated user.
        """
        date = self.request.query_params.get('date')
        owner_filter = Event.objects.filter(owner=self.request.user, date = self.request.query_params.get('date'))
        members_filter = Event.objects.filter(members=self.request.user, date = self.request.query_params.get('date'))

        return owner_filter | members_filter


class EventJoinAPIView(generics.UpdateAPIView):
    """
    If there are available seats, user join to event.
    
    """
    serializer_class = EventJoinSerializer
    permission_classes = [IsEventMember]
    queryset = Event.objects.all()
    lookup_field = 'id'

    def update(self, request, *args, **kwargs):
        instance = self.get_object()

        if instance.person_number <= instance.members.count():
            return Response({'success': False,
                             'message': 'There are no available seats.'},
                         status=status.HTTP_400_BAD_REQUEST)

        data = {'members': [request.user.id]}
        serializer = self.get_serializer(instance, data=data, partial=True)

        if serializer.is_valid(raise_exception=True):
            serializer.save()
        
            return Response(serializer.data, status=status.HTTP_200_OK)

        return Response({'success': False,
                         'message': 'User joining FAILED'},
                         status=status.HTTP_400_BAD_REQUEST)


class EventUnjoinAPIView(generics.UpdateAPIView):
    """
    User unjoin to event.
    
    """
    serializer_class = EventUnjoinSerializer
    permission_classes = [IsEventMember]
    queryset = Event.objects.all()
    lookup_field = 'id'

    def update(self, request, *args, **kwargs):
        instance = self.get_object()

        data = {'members': [request.user.id]}
        serializer = self.get_serializer(instance, data=data, partial=True)

        if serializer.is_valid(raise_exception=True):
            serializer.save()
        
            return Response(serializer.data, status=status.HTTP_200_OK)

        return Response({'success': False,
                         'message': 'User unjoining FAILED'},
                         status=status.HTTP_400_BAD_REQUEST)


class EventDate(generics.ListCreateAPIView):
    """
    Return list of events for certain date.
    
    """
    serializer_class = EventSerializer
    permission_classes = [permissions.IsAuthenticated]

    def get_queryset(self):
        date = self.request.query_params.get('date')
        if date:
            self.queryset = Event.objects.filter(date=date)
            return self.queryset
        else:
            return self.queryset


class EventAfterDate(generics.ListCreateAPIView):
    """
    Return list of events for certain date and future.
    
    """
    serializer_class = EventSerializer
    permission_classes = [permissions.IsAuthenticated]

    def get_queryset(self):
        date = self.request.query_params.get('date')
        if date:
            self.queryset = Event.objects.filter(date__gte=datetime.now().date())
            return self.queryset
        else:
            return self.queryset


class EventVisited(generics.ListCreateAPIView):
    """
    Return list of visited events for certain user.
    
    """
    serializer_class = EventSerializer
    permission_classes = [permissions.IsAuthenticated]

    def get_queryset(self):
        self.queryset = Event.objects.filter(members=self.request.user, date__lte=datetime.now().date())
        return self.queryset


class EventCreated(generics.ListCreateAPIView):
    """
    Return list of created by user events.
    
    """
    serializer_class = EventSerializer
    permission_classes = [permissions.IsAuthenticated]

    def get_queryset(self):
        self.queryset = Event.objects.filter(owner=self.request.user)
        return self.queryset


class EventFilters(generics.ListAPIView):
    """
    Return events for filter.
    There is filter by:
    * date
    * time
    * sports
    * free seats
    
    """
    serializer_class = EventSerializer
    permission_classes = [permissions.IsAuthenticated]

    def get_queryset(self):
        query = {}

        if self.request.query_params.get('date'):
            query['date'] = self.request.query_params.get('date')
        else:
            query['date__gte'] = datetime.now().date()

        if self.request.query_params.get('start_time'):
            query['start_time__gte'] = self.request.query_params.get('start_time')

        if self.request.query_params.get('sport'):
            query['sport__in'] = self.request.query_params.get('sport')
                
        if self.request.query_params.get('free_seats_gte'):
            query['free_seats__gte'] = self.request.query_params.get('free_seats_gte')
        
        if self.request.query_params.get('free_seats_lte'):
            query['free_seats__lte'] = self.request.query_params.get('free_seats_lte')

        self.queryset = Event.objects.filter(**query)
        
        return self.queryset

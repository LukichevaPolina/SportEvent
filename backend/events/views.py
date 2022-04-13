from django.contrib.auth.models import User
from events.models import Event
from events.permissions import IsOwnerOrReadOnly
from events.serializers import EventSerializer, EventJoinSerializer, EventUnjoinSerializer
from rest_framework import generics, status
from rest_framework import permissions
from rest_framework.response import Response
from datetime import datetime


class EventList(generics.ListCreateAPIView):
    queryset = Event.objects.all()
    serializer_class = EventSerializer
    permission_classes = [permissions.IsAuthenticated]

    def perform_create(self, serializer):
        serializer.save(owner=self.request.user)


class EventDetail(generics.RetrieveUpdateDestroyAPIView):
    queryset = Event.objects.all()
    serializer_class = EventSerializer
    permission_classes = [permissions.IsAuthenticated]


class EventSchedule(generics.ListAPIView):
    serializer_class = EventSerializer
    permission_classes = [permissions.IsAuthenticated]

    def get_queryset(self):
        """
        This view should return a list of all the events
        for the currently authenticated user.
        """
        return Event.objects.filter(members=self.request.user, date__gt=datetime.now().date())


class EventJoinAPIView(generics.UpdateAPIView):
    serializer_class = EventJoinSerializer
    permission_classes = [permissions.IsAuthenticated]
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
        
            return Response({'success': True,
                            'message': 'User joining success'},
                            status=status.HTTP_200_OK)

        return Response({'success': False,
                         'message': 'User joining FAILED'},
                         status=status.HTTP_400_BAD_REQUEST)


class EventUnjoinAPIView(generics.UpdateAPIView):
    serializer_class = EventUnjoinSerializer
    permission_classes = [permissions.IsAuthenticated]
    queryset = Event.objects.all()
    lookup_field = 'id'

    def update(self, request, *args, **kwargs):
        instance = self.get_object()

        data = {'members': [request.user.id]}
        serializer = self.get_serializer(instance, data=data, partial=True)

        if serializer.is_valid(raise_exception=True):
            serializer.save()
        
            return Response({'success': True,
                            'message': 'User joining success'},
                            status=status.HTTP_200_OK)

        return Response({'success': False,
                         'message': 'User joining FAILED'},
                         status=status.HTTP_400_BAD_REQUEST)
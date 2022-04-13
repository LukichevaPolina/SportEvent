from django.contrib.auth.models import User
from sports.models import Sport
from sports.serializers import SportSerializer
from rest_framework import generics
from rest_framework import permissions

class SportList(generics.ListCreateAPIView):
    queryset = Sport.objects.all()
    serializer_class = SportSerializer

    def perform_create(self, serializer):
        serializer.save()


class SportDetail(generics.RetrieveUpdateDestroyAPIView):
    queryset = Sport.objects.all()
    serializer_class = SportSerializer


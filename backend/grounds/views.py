from django.shortcuts import render
from rest_framework import generics, permissions, status
from grounds.models import Ground
from grounds.serializers import GroundSerializer

class GroundList(generics.ListCreateAPIView):
    queryset = Ground.objects.all()
    serializer_class = GroundSerializer
    #permission_classes = [permissions.IsAuthenticated]

    def perform_create(self, serializer):
        serializer.save()

    def get_serializer(self, *args, **kwargs):
        if "data" in kwargs:
            data = kwargs["data"]

            if isinstance(data, list):
                kwargs["many"] = True

        return super(GroundList, self).get_serializer(*args, **kwargs)

class GroundDetail(generics.RetrieveUpdateDestroyAPIView):
    queryset = Ground.objects.all()
    serializer_class = GroundSerializer


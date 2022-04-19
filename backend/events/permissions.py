from rest_framework import permissions


class IsOwnerOrReadOnly(permissions.BasePermission):
    """
    Custom permission to only allow owners of an object to edit it.
    """

    def has_permission(self, request, view):
        # Read permissions are allowed to any request,
        # so we'll always allow GET, HEAD or OPTIONS requests.
        if request.method in [permissions.SAFE_METHODS]:
            return True

        # Owner can't change field 'members'
        if 'members' in request.data and request.method == 'PATCH':
            return False
        return True

    def has_object_permission(self, request, view, obj):
        # Read permissions are allowed to any request,
        # so we'll always allow GET, HEAD or OPTIONS requests.
        if request.method in [permissions.SAFE_METHODS]:
            return True

        # Owner can't change field 'members'
        if 'members' in request.data and request.method == 'PATCH':
            return False
        return obj.owner == request.user

class IsEventMember(permissions.BasePermission):
    """
    Custom permission to only members of event change 'members' field
    """

    def has_permission(self, request, view):
        # Read permissions are allowed to any request,
        # so we'll always allow GET, HEAD or OPTIONS requests.
        if request.method in [permissions.SAFE_METHODS]:
            return True

        # Write permissions are only allowed to the patching of 'member' field.
        # request.data is empty because data contains in request.user
        if request.data == {} and request.method == 'PATCH':
            return True
        return False

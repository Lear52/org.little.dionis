
1 ���� ������ ������������� goto 11
2 �������� ip �������
3 user=userlist.isGuest(ip �������)
3 if(user!=null) goto END
4 �������� type �������������� (type_auth)
5 ��������� ��������� http.header
6 if( http.header.auth == type_auth) goto 9
7 �������� 401+type_auth
8 goto 5
9 �������� user
10 if( user.isCheck() ==false) goto 7
11 (������ �������������)=true;
12 ����������� �������� � �������
13 if (http.header.cod==401)(������ �������������)=false;
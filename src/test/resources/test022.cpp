void MyFunction()
{
  int i = 0;
  switch( i )
  {
  case 0:
    ++i;
    break;
  case 1:
    --i;
    break;
  default:
    i = 0;
    break;
  }
  const int j = i * 2;
}

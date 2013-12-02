KitKatStepCounter
=================

Simple app that counts the steps using the new Kit Kat Android API for accessing the step counter.

This code includes a service that will tun on boot and connect to the step service.  It then saves those values into the Database for that day.  The UI will simply display the accumulated step count saved for that day.

Next steps:

1) Figure out exactly how to handle step counts when the phone is rebooted in the middle fo the day.

2) Show just the count for the current day, not the accumulated total.



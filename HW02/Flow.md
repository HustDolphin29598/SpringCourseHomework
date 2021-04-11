**Basic flow and status for movie booking system**

1. User choose the movie
2. Booking object is initialized, with STATUS `INIT`.</br> 
   a. If user cancel booking, the **booking** object turns to STATUS `CANCELED` --> flow ends. </br>
   b. If it takes too long with no response from user then the **booking** object turns to STATUS `TIMEOUT` --> flow ends.
3. Payment object is initialized, with STATUS `INIT`.</br> 
   a. If user cancel payment, the **payment** and **booking** objects turn to STATUS `CANCELED` --> flow ends. </br>
   b. If it takes too long with no response from user then the **payment** and **booking** objects turn to STATUS `TIMEOUT` --> flow ends. <br>
   c. If payment fails, the **payment** object and **booking** object turn to STATUS `FAILED` --> flow ends. <br>
   d. If payment succeeds, the **payment** object and **booking** object turn to STATUS `SUCCESS` --> flow ends.

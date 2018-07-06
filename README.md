# PianoBreaker
PianoTiles 2 bot written in Java (For Android OS)

Demo video: https://youtu.be/i_b5RqqedYE

Developed by @sirmordred Oguzhan Yigit

**USAGE INSTRUCTIONS:**

- Download precompiled pianobreaker.jar and copy it into the phone (destination folder on your phone should be internal storage not external like removable sd card etc.)

- Make sure you enabled "USB Debugging" option from Development Settings on your phone

- Connect your phone to your PC via usb cable and type "adb devices" command into PC's terminal to make sure device is detected by PC succesfully (it should say something like "2345234857845 device") if you dont know ADB (android debug bridge) google it before doing this step

- Open "Piano Tiles 2" app on your phone and select classic mode(only white and black tiles)

- Type the following command **adb shell CLASSPATH=/sdcard/pianobreaker.jar /system/bin/app_process /system/bin com.pianobreaker.PianoBreaker 29**
NOTE: number 29 in the above command is the execution time limit of script (as second), you can choose different values like 5 second or 15 second or whatever you want, as the "classic mode" in the game gives us 30 second as maximum, i choosed 29

- After typing the command, tap into first tile of game and voilaa, you did it, congrats :)

Enjoy

**BUILD INSTRUCTIONS:**

- Prepare AOSP build environment in your pc as described here https://source.android.com/setup/build/initializing and choose a working AOSP branch (like android-8.1.0_r41 or 7.1 or 6.0) and sync it

- Enclose **Android.mk** and **src** folder via another top folder (you can name it whatever you want) and place that folder into device/generic/arm64 folder in AOSP tree

- Enter **PRODUCT_PACKAGE := pianobreaker** into AndroidProduct.mk (build rule)

- Type  **. build/envsetup.sh** and **lunch** and select number of **generic_arm64**

- As a final step, type **make pianobreaker**

- After compilation process, the .jar output file should be at **FOLDER_OF_YOUR_AOSP_TREE/out/device/xx/xxx/pianobreaker.jar**

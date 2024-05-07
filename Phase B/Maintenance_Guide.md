# Maintenance Guide

## Requirements

### Android

- Android pyshical device
  - Support of `minSdk=26` (Android 8.0).
  - Bluetooth connection is required, compliant with the BLE (Bluetooth Low Energy) standard.

### Everysight

- Maverick Glasses (For debug only).
- A valid (non-expired) `app.key`.
  - In order to connect the glasses, the Everysight's SDK validates (with the `app.key`) that the app is authorized to use the glasses.
  - If it expires please contact support@everysight.com.
- SDK libraries [click here to download](https://github.com/everysight-maverick/sdk/tree/main).

## Setup

1. We recommend using Android Studio as your IDE.
2. Place your valid `app.key` in the `assets` folder.
3. Place the SDK libraries inside the [libs](./code/ARWorkout/app/libs/) folder.
4. Turn on your Maverick glasses.
5. Run the app on a supported Android Device.

## Edit Workouts

In case you want to edit the predefined workouts you should change the [Constants.kt](./code/ARWorkout/app/src/main/java/com/ofekyuval/arworkout/Constants.kt).

Edit the variable 'val availableWorkouts' with the help of this template:

```kotlin title="Edit Workouts"
val workoutDrawableRes = R.drawable.boy // replace with any drawable res you want
val exerciseDurationMs = 30000L // 30 seconds, can be changed per exercise
val numberOfReps = 10 // can be changed per exercise
Workout("REPLACE_WITH_WORKOUT_NAME", workoutDrawableRes, arrayOf(
    Exercise("REPLACE_WITH_EXERCISE_NAME", JumpActivity.instance, exerciseDurationMs, numberOfReps),
    // ... Add more exercises as needed
)),
```

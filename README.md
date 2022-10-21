# Power-Play-Code
20313 code repository for the 2022-2023 FIRST Tech Challenge Power Play season.

## Files

### Pipelines
#### Old Chassis
OldChassisPipeline.java -- deprecated/broken pipeline, initial pipeline for mounting the camera on the old chassis

SleeveDetection.java -- a stock pipeline copied from github.com/KookyBotz/PowerPlaySleeveDetection

SleevePipeline.java -- modification of OldChassisPipeline, also broken

TestPipeline.java -- working pipeline with solid color identification: programmed for black, magenta, and green, but can be tuned for other colors too


#### New Chassis
HSVPipeline.java -- a tester class using an HSV colorspace

Pipeline.java -- deprecated/broken, initial pipeline using summations of the submat


### Autonomous classes
AutoCam.java -- autonomous parking program using the camera: PROGRAMMED FOR OLD PIPELINE, MUST BE UPDATED


### TeleOp classes
Drive.java -- driving class, only designed for four directional movement + rotation with mecanum wheels

LinearSlideTest.java -- simple tester class for goBilda two stage viper slide

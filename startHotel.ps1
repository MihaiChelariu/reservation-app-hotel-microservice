# Define the directory where the JAR file is expected
$jarDirectory = "hotel\target"

# Check if the directory exists
if (Test-Path $jarDirectory) {
    # Navigate to the directory
    Set-Location $jarDirectory

    # Find the JAR file in the directory
    $jarFile = "hotel-0.0.1-SNAPSHOT.jar"

    # Check if a JAR file is found
    if (Test-Path $jarFile) {
        Write-Host "Running the Spring Boot application: $jarFile"
        # Run the JAR file
        & java -jar $jarFile
    } else {
        Write-Host "No JAR file found in the '$jarDirectory' directory. Build the project first."
    }
} else {
    Write-Host "Directory '$jarDirectory' not found. Build the project first."
}
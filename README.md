# SMS DND Manager

An Android application that listens for SMS messages from authorized contacts and automatically disables Do Not Disturb (DND) mode and adjusts ringer volume based on special activation codes.

## Features

- **SMS Activation**: Receive SMS commands in format `undndXX` (e.g., `undnd50`, `undnd100`)
- **Authorized Numbers**: Manage a whitelist of phone numbers that can trigger DND deactivation
- **Volume Control**: Set ringer volume from 0% (mute) to 100% (maximum)
- **Activity Log**: Track all DND deactivation events with timestamps
- **Secure Storage**: Phone numbers stored with encrypted preferences
- **Confirmation SMS**: Optional reply SMS confirming volume changes

## Use Cases

- Parents reaching children who have muted their phones
- On-call professionals needing guaranteed reachability
- Emergency contacts accessing someone in DND mode
- Caregivers monitoring elderly family members

## Technical Architecture

```
app/
├── data/
│   ├── local/          # Encrypted SharedPreferences storage
│   └── repository/     # Repository implementations
├── domain/
│   ├── model/          # Domain models (SmsMessage, VolumeCommand, etc.)
│   ├── repository/     # Repository interfaces
│   └── usecase/        # Business logic use cases
├── presentation/
│   ├── screen/         # UI screens (Compose)
│   ├── viewmodel/      # ViewModels
│   ├── theme/          # Material3 theme
│   └── navigation/     # Navigation graph
├── receiver/           # SMS and Boot broadcast receivers
└── di/                 # Hilt dependency injection modules
```

### Key Components

- **ProcessSmsUseCase**: Parses SMS messages and executes volume/DND changes
- **SmsBroadcastReceiver**: Receives incoming SMS broadcasts
- **EncryptedPreferencesDataSource**: Secure local storage
- **Clean Architecture**: Separation of concerns with domain/data/presentation layers

## Requirements

- Android 8.0+ (API 26)
- Permissions:
  - `RECEIVE_SMS` - Receive SMS broadcasts
  - `READ_SMS` - Read message content
  - `MODIFY_AUDIO_SETTINGS` - Change ringer volume
  - `ACCESS_NOTIFICATION_POLICY` - Modify DND settings

## Installation

1. Clone the repository
2. Open in Android Studio
3. Build and run on a device or emulator
4. Grant all required permissions when prompted

## Usage

1. **Add Authorized Numbers**:
   - Open the app
   - Go to "Authorized Numbers" tab
   - Tap the + button to add a phone number

2. **Send Activation SMS**:
   - From an authorized number, send SMS: `undnd50`
   - The app will disable DND and set volume to 50%
   - Valid range: `undnd0` (mute) to `undnd100` (max)

3. **View Activity Log**:
   - Go to "Activity Log" tab to see all deactivation events

## SMS Command Format

| Command | Action |
|---------|--------|
| `undnd0` | Mute the phone (0% volume) |
| `undnd25` | Set volume to 25% |
| `undnd50` | Set volume to 50% |
| `undnd75` | Set volume to 75% |
| `undnd100` | Set volume to maximum |

Commands are case-insensitive and can be embedded in longer messages.

## Development

### Build

```bash
./gradlew assembleDebug
```

### Run Tests

```bash
./gradlew test
```

### Architecture Decision Records

See [`PRD/adr/`](PRD/adr/) for architecture decisions.

## Project Structure

This project follows the KiloCode AI Template structure:

- `PRD/` - Product Requirement Documents for features
- `PRD/done/` - Completed features
- `PRD/adr/` - Architecture Decision Records
- `.kilocode/workflows/` - Workflow definitions
- `.kilocode/skills/` - Reusable skill definitions

## License

MIT License - See LICENSE file for details

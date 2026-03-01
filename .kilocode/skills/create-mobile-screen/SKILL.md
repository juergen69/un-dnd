---
name: create-mobile-screen
description: Create a new mobile screen/activity with proper structure, navigation, state management, and accessibility.
---

# Create Mobile Screen

## When to Use
- When implementing a new screen, activity, or fragment for a mobile app
- When adding a new view to an existing mobile application

## Steps

### 1. Analyze Requirements
- Read the task file and acceptance criteria
- Identify the screen's purpose, data requirements, and user interactions
- Determine navigation flow (how users reach and leave this screen)

### 2. Design the Screen
- Define the UI layout and component hierarchy
- Plan state management (ViewModel, StateFlow, LiveData, or framework equivalent)
- Identify API calls or data sources needed
- Plan for different screen sizes and orientations

### 3. Implement

#### For Android (Kotlin + Jetpack Compose):
- Create a composable function for the screen
- Create a ViewModel for state management
- Wire up navigation using Navigation Compose
- Handle loading, error, and empty states

#### For Android (Kotlin + XML Views):
- Create the Activity/Fragment class
- Create the XML layout file
- Create a ViewModel for state management
- Wire up view binding and navigation

#### For Cross-Platform (Flutter):
- Create a widget class for the screen
- Create a state management solution (BLoC, Provider, Riverpod)
- Wire up navigation (GoRouter or Navigator)
- Handle loading, error, and empty states

#### For Cross-Platform (React Native):
- Create the screen component
- Create the state management slice/context
- Wire up navigation (React Navigation)
- Handle loading, error, and empty states

### 4. Accessibility
- Ensure all interactive elements have content descriptions
- Support screen readers (TalkBack / VoiceOver)
- Ensure minimum touch target sizes (48dp)
- Support dynamic font sizes
- Test with high contrast / dark mode

### 5. Testing
- Write UI tests for the screen
- Write unit tests for the ViewModel/state logic
- Test on multiple screen sizes and orientations
- Test offline behavior if applicable

## Quality Checklist
- [ ] Screen follows the app's design system and patterns
- [ ] State management is clean (no business logic in the view layer)
- [ ] Loading, error, and empty states are handled
- [ ] Navigation works correctly (back, deep links)
- [ ] Accessibility requirements met
- [ ] Screen works in both portrait and landscape
- [ ] Performance is acceptable (no jank on scroll, fast render)

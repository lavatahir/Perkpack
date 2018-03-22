# SYSC 4806 Project - PerkPack

PerkPack is a perk/card organizer.

## Notes
- On cloning the repository, go to the React app root folder (currently react-frontend) and run npm install.

## Milestone 2
- Switched from H2 DB to PostgresSQL (except for tests, which still use H2)
- Added significantly more tests (both model tests and controllers tests)
- Added nicer looking front end
- Secure user login, with passwords being salted before saving in database
- Can add cards to an account

## Milestone 1
- Perk creation and perk voting (changes are saved to the server)
- User creation
- Card creation

The idea is that a user signs up, creating a User object. Users can then add cards to their account. If the card doesn't exit, 
the user can then add perks to the card.

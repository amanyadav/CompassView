# CompassView

A custom view library which you can use in your project to show directions.

## Screenshots

[![Main screen][screen1]

## Usage
Add this view in your layout file.
```xml
<net.androidsrc.darkcompass.CompassView
        android:id="@+id/compassView"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
```
Reference the view in java code and then set angle.
```java
CompassView compassView=(CompassView)findViewById(R.id.compassView);
compassView.setAngle(angle);
```
CompassView will smoothly transistion to that angle. \m/

## License

    This program is free software: you can redistribute it and/or modify it
    under the terms of the GNU General Public License as published by the Free
    Software Foundation, either version 3 of the License, or (at your option)
    any later version.

    This program is distributed in the hope that it will be useful, but WITHOUT
    ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
    FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
    more details.

    You should have received a copy of the GNU General Public License along
    with this program.  If not, see <http://www.gnu.org/licenses/>.
    
[screen1]: screenshots/shot1.png
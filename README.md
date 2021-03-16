
# CSCI 1302 - Minesweeper Alpha v2020.sp

![Approved for: Spring 2020](https://img.shields.io/badge/Approved%20for-Spring%202020-blue)

![Unrelated image of mine."](mine.jpg)

**DUE FRI 2020-01-31 (Jan 31) @ 11:55 PM.**

This repository contains the skeleton code for the Minesweeper Alpha project
assigned to the students in the Spring 2020 CSCI 1302 classes
at the University of Georgia. 

**Please read the entirety of this file before beginning your project.**

**Seriously. Read this entire file *before* starting.**

## Table of Contents

* [Academic Honesty](#academic-honesty)
* [Updates](#updates)
* [Project Description](#project-description)
  * [Note Concerning No Recursion](#note-concerning-no-recursion) 
  * [Minesweeper Overview](#minesweeper-overview)
  * [The Grid and Interface](#the-grid-and-interface)
    * [The Grid](#the-grid)
    * [The User Interface](#the-user-interface)
    * [Command Syntax Format](#command-syntax-format)
    * [Revealing a Square](#revealing-a-square)
    * [Mark Command](#mark-command)
    * [Guess Command](#guess-command)
    * [No Fog Command](#no-fog-command)
    * [Help Command](#help-command)
    * [Quit Command](#quit-command)
    * [Player Wins](#player-wins)
    * [Displaying Errors](#displaying-errors)
  * [Seed Files](#seed-files)
* [Project Requirements & Grading](#project-requirements--grading)
  * [Functional Requirements](#functional-requirements)
  * [Non-Functional Requirements](#non-functional-requirements)
  * [Extra Credit Requirements](extra-credit-requirements)
* [Suggestions](#suggestions)
* [How to Download the Project](#how-to-download-the-project)
* [Submission Instructions](#submission-instructions)
* [Appendix](#appendix)
  * [Minefield Output Examples](#minefield-output-examples)
  * [Format Strings](#format-strings)
  * [Number of Digits](#number-of-digits)

## Academic Honesty

**You agree to the Academic Honesty policy as outlined in the course syllabus.**
In accordance with this notice, I must caution you **not** to
fork this repository on GitHub if you have an account. Doing so will more than
likely make your copy of the project publicly visible. Please follow the
instructions contained in the [How to Download the Project](#how-to-download-the-project)
section below in order to do your development on nike. Furthermore, you must adhere
to the copyright notice and licensing information at the bottom of this document.

## Updates

Updates will be posted here.
If there has been an update and you have already cloned the project to Nike,
then you can update your copy of the project using the `$ git pull`
command while inside of your project directory. 

## Project Description

This first project is meant to ensure that you are able to apply and extend
your prerequisite knowledge as well as introduce you to developing and testing
a Java application in a Linux environment (i.e., the Nike development
server). Many aspects of this project may be new to you. You will be asked to do things
that you have never been given explicit directions for before.
This is just a part of software development. Sometimes you need to research
how to solve a problem in order to implement a solution. That being said,
the material included in this document should hopefully answer the majority
of your questions.

Your goal is to develop a non-recursive, non-GUI (GUI = Graphical User
Interface) version of the game called **Minesweeper**. The code for this game will
be organized in such a way that the recursive elements of Minesweeper can
be added at a later point in time, if desired. It will also be organized so that
you can add a GUI to it later as well. Interestingly, the organization of some of the
classes in this project will also introduce you to some elementary aspects of
game programming.

If you are not familiar with Minesweeper as a game, then please consult the
[Wikipedia entry](https://en.wikipedia.org/wiki/Minesweeper_(video_game))
for the game, ignoring any mentions of recursion. Once you're familiar
with the basic gameplay, then continue reading this project description.

### Note Concerning "No Recursion"

In a traditional game of Minesweeper, when the player "reveals" a square
that does not contain a mine, **two** things happen:

1. A number representing the number of mines in the (up to) eight
   adjacent squares is placed in the revealed square; and

1. If the number of adjacent mines is zero, then game goes ahead
   and "reveals" all of the (up to) eight adjacent squares.

The second part mentioned above can cause one reveal made by the user
to result in multiple reveals in the minefield. **This behavior is what
the literature is referring to when it talks about recursion in Mineweeper.**
Your game does not need to support this behavior. If the user reveals
one square, then, at most, one square is revealed in the minefield.

### Minesweeper Overview

In your Minesweeper, the player is initially presented with a grid of
undifferentiated squares. Either some randomly-selected squares or seed-selected
squares (more on seeds later) are designated to contain mines. The
size of the grid and the number of mines are set in advance by a
seed file that the user specifies as a command-line argument to your
program. The ratio of the number of mines to the grid size is often used as a
measure of an individual game's difficulty. The grid size can also be
represented in terms of the number of rows and columns in the grid.
In this project description, we may refer to the _grid_ or to the
_minefield_. Both of these terms mean the same thing. Furthermore,
we will use the term _square_ to denote a location in the minefield, even
in situations where a location may be visually rectangular instead
of perfectly square.

The game is played in rounds. During each round, the player is presented with
the grid, the number of rounds completed so far, as well as a prompt. The player
has the option to do 5 different things, each of which is briefly listed
below and explained in great detail in later sections:

 1. Reveal a square on the grid.
 2. Mark a square as potentially containing a mine.
 3. Mark a square as definitely containing a mine.
 4. Lift the fog of war (cheat code).
 5. Display help information.
 6. Quit the game.

When the player reveals a square of the grid, different things can happen:

* If the revealed square contains a mine, then the player loses the game.

* If the revealed square does not contain a mine, then a digit is instead displayed
  in the square, indicating how many adjacent squares contain mines. Typically,
  there are 8 squares adjacent to any given square, unless the square lies on an
  edge or corner of the grid. The player uses this information to deduce the contents
  of other squares, and may perform any of the first three options in the list presented above.

* When the player marks a square as potentially containing a mine, a `?` is displayed
  in the square. This provides the user with a way to note those places that they
  believe may contain a mine but are not sure enough to mark as definitely containing
  a mine.

* When the player marks a square as definitely containing a mine, a flag, denoted
  by the character `F`, is displayed in the square.

To simplify the game mechanics, **the player may mark, guess, or reveal any square in the grid,
even squares that have already been marked or revealed.** For example, the player may issue a 
command to mark, guess, or reveal a square, regardless of its current state. The logic for 
determining what happens to the square is always the same. For example, if a square has been 
revealed and the user marks it as definitely containing a mine then a round is consumed and the
square should be marked. The user would then have to reveal this square again later. This may
not be consistent with how you've played Minesweeper in the past but it will make it easier
to code.

The game is won when **all** of the following conditions are met:

* All squares containing a mine are marked as _definitely_ containing a mine; and

* All squares not containing a mine are revealed.

At the end of the game, the player is presented with a score. Let `rows`, `cols`,
and  `rounds` denote the number of rows in the grid, columns in the grid, and
number of rounds completed, respectively. A round is defined as one successful
iteration of the main game loop. Therefore, only valid commands result in a round 
being consumed. To be clear, _rounds_ is not quite the same as the number of commands 
entered (some may be invalid); however, it should be less than or equal to that number.

The player's score is calculated as follows:

```java
score = 100.0 * rows * cols / rounds;
```

A score of `100` would denote a perfect game. In this version of Mineweeper, it should
not be possible for the player to win the game in less than `(rows * cols)`-many rounds.
Therefore, any game in which the player exceeds that many rounds would result in a score
that is less than `100`. When displaying the score, the number should always be printed
with two digits following the decimal point.

### The Grid and Interface

When the game begins, the following **welcome banner** should be displayed to the player
once and only once:

```
        _
  /\/\ (_)_ __   ___  _____      _____  ___ _ __   ___ _ __
 /    \| | '_ \ / _ \/ __\ \ /\ / / _ \/ _ \ '_ \ / _ \ '__|
/ /\/\ \ | | | |  __/\__ \\ V  V /  __/  __/ |_) |  __/ |
\/    \/_|_| |_|\___||___/ \_/\_/ \___|\___| .__/ \___|_|
                 A L P H A   E D I T I O N |_| v2020.sp
```

Take care when printing this message out to the screen. You will probably need
to escape some of the characters in order for them to show up correctly.

In this Minesweeper game, **the size of the grid is restricted to at least `5`
rows and `5` columns**, but may be greater. The number of rows and columns need not be the same.
Rows and columns are indexed starting at `0`. Therefore, in a `10`-by-`10` (rows-by-columns),
the first row is indexed as `0` and the last row is indexed as `9` (similarly for columns).

#### The Grid

Let's assume we are playing a `5`-by-`5` game of Minesweeper. When the game
starts, the interface should look like this:

```

 Rounds Completed: 0

 0 |   |   |   |   |   |
 1 |   |   |   |   |   |
 2 |   |   |   |   |   |
 3 |   |   |   |   |   |
 4 |   |   |   |   |   |
     0   1   2   3   4

minesweeper-alpha:
```

Let's assume we are playing a `10`-by-`10` game of Minesweeper. When the game
starts, the interface should look like this:

```

 Rounds Completed: 0

 0 |   |   |   |   |   |   |   |   |   |   |
 1 |   |   |   |   |   |   |   |   |   |   |
 2 |   |   |   |   |   |   |   |   |   |   |
 3 |   |   |   |   |   |   |   |   |   |   |
 4 |   |   |   |   |   |   |   |   |   |   |
 5 |   |   |   |   |   |   |   |   |   |   |
 6 |   |   |   |   |   |   |   |   |   |   |
 7 |   |   |   |   |   |   |   |   |   |   |
 8 |   |   |   |   |   |   |   |   |   |   |
 9 |   |   |   |   |   |   |   |   |   |   |
     0   1   2   3   4   5   6   7   8   9

minesweeper-alpha:
```

Please note that the in either example, the first, third, and second-to-last lines are blank 
(the lines before and after "Rounds Completed" and the line before the prompt).
All other lines, except the last line containing the prompt, start with one blank space.
The line containing the prompt contains an extra space after the `:`
so that **when the user types in a command, the text does not touch the
`:`.** Multiple output examples are provided in the [Appendix](#minefield-output-examples)
of this project description for your convenience.

The output is a little tricky when either the number of rows or the number of
columns becomes multiple digits. In this case, padding should be added so that
row numbers, column numbers, and mine field contents are padded with whitespace
in a way that accomodates the multiple digit index values. Additionally, when
padding is performed, **all affected index values and square contents are
to be right aligned**. For example, let's assume we are playing a `12`-by-`5`
game of Minesweeper. When the game starts, the interface should look like this:

```

 Rounds Completed: 0

  0 |   |   |   |   |   |
  1 |   |   |   |   |   |
  2 |   |   |   |   |   |
  3 |   |   |   |   |   |
  4 |   |   |   |   |   |
  5 |   |   |   |   |   |
  6 |   |   |   |   |   |
  7 |   |   |   |   |   |
  8 |   |   |   |   |   |
  9 |   |   |   |   |   |
 10 |   |   |   |   |   |
 11 |   |   |   |   |   |
      0   1   2   3   4

minesweeper-alpha:
```

In this example, the row numbers needed to be padded to accomodate the `10` and `11` index
values, however, the column numbers and square contents did not require any padding.

Now let's assume we are playing a `5`-by-`12` game of Minesweeper. When the game starts,
the interface should look like this:

```

 Rounds Completed: 0

 0 |    |    |    |    |    |    |    |    |    |    |    |    |
 1 |    |    |    |    |    |    |    |    |    |    |    |    |
 2 |    |    |    |    |    |    |    |    |    |    |    |    |
 3 |    |    |    |    |    |    |    |    |    |    |    |    |
 4 |    |    |    |    |    |    |    |    |    |    |    |    |
      0    1    2    3    4    5    6    7    8    9   10   11

minesweeper-alpha:
```

In this example, the column numbers and square contents needed to be padded
due to column index values `10` and `11`, however, the row index values did
not require any padding.

Finally, let's assume we are playing a `11`-by-`11` game of Minesweeper. When the
game starts, the interface should look like this:

```

 Rounds Completed: 0

  0 |    |    |    |    |    |    |    |    |    |    |    |
  1 |    |    |    |    |    |    |    |    |    |    |    |
  2 |    |    |    |    |    |    |    |    |    |    |    |
  3 |    |    |    |    |    |    |    |    |    |    |    |
  4 |    |    |    |    |    |    |    |    |    |    |    |
  5 |    |    |    |    |    |    |    |    |    |    |    |
  6 |    |    |    |    |    |    |    |    |    |    |    |
  7 |    |    |    |    |    |    |    |    |    |    |    |
  8 |    |    |    |    |    |    |    |    |    |    |    |
  9 |    |    |    |    |    |    |    |    |    |    |    |
 10 |    |    |    |    |    |    |    |    |    |    |    |
       0    1    2    3    4    5    6    7    8    9   10

minesweeper-alpha:
```

In this example, padding was required for the row index values, column index
values, and the square contents.

**Before you start writing the code to format your output,** please
read the [Format Strings](#format-strings) and [Number of Digits](#number-of-digits)
sections in the Appendix. They provide some code examples that should be
helpful.

#### The User Interface

The possible commands that can be entered into the game's prompt as well as their
syntax are listed in the subsections below. Commands with leading or trailing
whitespace are to be interpreted as if there were no leading or trailing
whitespace. For example, the following two examples should be interpreted the
same:

```
minesweeper-alpha: help
minesweeper-alpha:         help
```

Although it's hard to see in the example above, trailing whitespace should
also be ignored. That is, if the user types ` ` one or more times before
pressing the `RET` (return) key, then those extra whitespaces should be
ignored.

The different parts of a command are known as tokens. The `help`
command, for example, only has one token. Other commands, such as the
`mark` (seen below) have more than one token because other
pieces of information are needed in order to interpret the command. As a quick
example (which will be explored in more depth below), the player can
mark the square at coordinate (0,0) using `mark` as follows:

```
minesweeper-alpha: mark 0 0
```

In the above example, you can see that the `mark` command has three
tokens. A command with more than one token is still considered syntactically
correct if there is more than one white space between tokens. For example, the
following four examples should be interpreted the same:

```
minesweeper-alpha: mark 0 0
minesweeper-alpha: mark     0  0
minesweeper-alpha:     mark 0 0
minesweeper-alpha:   mark     0  0
```

As a reminder, trailing whitespace is ignored.

#### Command Syntax Format

In the sections below, each command will the syntax format that it must adhere
to in order to be considered correct. Syntactically incorrect commands are
considered an error. Information about displaying errors to the player is
contained in a section below.

**Please do not confuse this syntax with regular expressions, a topic that
will not be covered in this course.** You are NOT meant to put this weird
looking syntax into any code. It is purely meant to convey to you, the reader,
what is and what is not valid input for a given command.

In a syntax format string, one or more white space is represented as a
`-`. Command tokens are enclosed in `[]` braces. If the
contents of a token are surrounded by `""` marks, then that token can
only take on that literal value. If more than one literal value is accepted for
a token, then the quoted literals are separated by `/`. If the
contents of a token are surrounded by `()` marks, then that token can
only take on a value of the type expressed in parentheses.

Syntax format strings are provided in this document in order to help you, the
student, understand how syntactically correct commands could potentially be
inputted by the player. These strings do not directly correspond to anything in
the Java programming language. You should be able to use the information
provided in these syntax format strings to parse commands entered by the
user.

#### Revealing a Square

In order to reveal a square, the `reveal` or `r` command
is used. The syntax format for this command is as follows: `-["reveal"/"r"]-[(int)]-[(int)]-`.
The second and third tokens indicate the row and column indices, respectively,
of the square to be revealed.

Let's go back to our `10`-by-`10` example. Suppose that we secretly know that there is
a mine in squares (1,1) and (1,3). Now suppose that the player wants to reveal
square (1, 2). Here is an example of what that might look like.

```

 Rounds Completed: 0

 0 |   |   |   |   |   |   |   |   |   |   |
 1 |   |   |   |   |   |   |   |   |   |   |
 2 |   |   |   |   |   |   |   |   |   |   |
 3 |   |   |   |   |   |   |   |   |   |   |
 4 |   |   |   |   |   |   |   |   |   |   |
 5 |   |   |   |   |   |   |   |   |   |   |
 6 |   |   |   |   |   |   |   |   |   |   |
 7 |   |   |   |   |   |   |   |   |   |   |
 8 |   |   |   |   |   |   |   |   |   |   |
 9 |   |   |   |   |   |   |   |   |   |   |
     0   1   2   3   4   5   6   7   8   9

minesweeper-alpha: r 1 2

 Rounds Completed: 1

 0 |   |   |   |   |   |   |   |   |   |   |
 1 |   |   | 2 |   |   |   |   |   |   |   |
 2 |   |   |   |   |   |   |   |   |   |   |
 3 |   |   |   |   |   |   |   |   |   |   |
 4 |   |   |   |   |   |   |   |   |   |   |
 5 |   |   |   |   |   |   |   |   |   |   |
 6 |   |   |   |   |   |   |   |   |   |   |
 7 |   |   |   |   |   |   |   |   |   |   |
 8 |   |   |   |   |   |   |   |   |   |   |
 9 |   |   |   |   |   |   |   |   |   |   |
     0   1   2   3   4   5   6   7   8   9

minesweeper-alpha:
```

After the player correctly entered the command `r 1 2`, the state of
the game updates (e.g., number of rounds completed, the grid, etc.), and the
next round happens. Since there was no mine in square (1,2), the player does not
lose the game. Also, since the total number of mines in the 8 cells directly 
adjacent to square (1,2) is 2, the number 2 is now placed in that cell.

If the player reveals a square containing a mine, then the following message
should be displayed and the program should terminate gracefully:

```

 Oh no... You revealed a mine!
  __ _  __ _ _ __ ___   ___    _____   _____ _ __
 / _` |/ _` | '_ ` _ \ / _ \  / _ \ \ / / _ \ '__|
| (_| | (_| | | | | | |  __/ | (_) \ V /  __/ |
 \__, |\__,_|_| |_| |_|\___|  \___/ \_/ \___|_|
 |___/

```

Yeah, that's old school ASCII art. Please note that the first and last lines are
blank. Also note that the second line begins with a single white space. All other
lines should be copied verbatim from this document (e.g., you can just copy and
paste it using your plain text editor).

The program should exit gracefully. This means that exit code should be zero.
For example, the following snippet will accomplish this:

```java
System.exit(0);
```

If your program exits from a "game over" with any return codes other than zero 
(e.g., if your game crashes), then some points will be deducted. After the program 
terminates, a user should be able to confirm the exit status of `0` using `echo $?`.

#### Mark Command

In order to mark a square as definitely containing a mine, the
`mark` or `m` command is used. The syntax format for this
command is as follows: `-["mark"/"m"]-[(int)]-[(int)]-`.
The second and third tokens indicate the row and column indices, respectively,
of the square to be revealed.

Let's go back to our `10`-by-`10` example. Suppose that the player wants to mark
square (1, 2). Here is an example of what that might look like.

```

 Rounds Completed: 0

 0 |   |   |   |   |   |   |   |   |   |   |
 1 |   |   |   |   |   |   |   |   |   |   |
 2 |   |   |   |   |   |   |   |   |   |   |
 3 |   |   |   |   |   |   |   |   |   |   |
 4 |   |   |   |   |   |   |   |   |   |   |
 5 |   |   |   |   |   |   |   |   |   |   |
 6 |   |   |   |   |   |   |   |   |   |   |
 7 |   |   |   |   |   |   |   |   |   |   |
 8 |   |   |   |   |   |   |   |   |   |   |
 9 |   |   |   |   |   |   |   |   |   |   |
     0   1   2   3   4   5   6   7   8   9

minesweeper-alpha: m 1 2

 Rounds Completed: 1

 0 |   |   |   |   |   |   |   |   |   |   |
 1 |   |   | F |   |   |   |   |   |   |   |
 2 |   |   |   |   |   |   |   |   |   |   |
 3 |   |   |   |   |   |   |   |   |   |   |
 4 |   |   |   |   |   |   |   |   |   |   |
 5 |   |   |   |   |   |   |   |   |   |   |
 6 |   |   |   |   |   |   |   |   |   |   |
 7 |   |   |   |   |   |   |   |   |   |   |
 8 |   |   |   |   |   |   |   |   |   |   |
 9 |   |   |   |   |   |   |   |   |   |   |
     0   1   2   3   4   5   6   7   8   9

minesweeper-alpha:
```

After the player correctly entered the command `m 1 2`, the state of
the game updates (e.g., number of rounds completed, the grid, etc.), and the
next round happens.

#### Guess Command

In order to mark a square as potentially containing a mine, the
`guess` or `g` command is used. The syntax format for this
command is as follows: `-["guess"/"g"]-[(int)]-[(int)]-`.
The second and third tokens indicate the row and column indices, respectively,
of the square to be revealed.

Let's go back to our `10`-by-`10` example. Suppose that the player wants to guess
square (1, 2). Here is an example of what that might look like.

```

 Rounds Completed: 0

 0 |   |   |   |   |   |   |   |   |   |   |
 1 |   |   |   |   |   |   |   |   |   |   |
 2 |   |   |   |   |   |   |   |   |   |   |
 3 |   |   |   |   |   |   |   |   |   |   |
 4 |   |   |   |   |   |   |   |   |   |   |
 5 |   |   |   |   |   |   |   |   |   |   |
 6 |   |   |   |   |   |   |   |   |   |   |
 7 |   |   |   |   |   |   |   |   |   |   |
 8 |   |   |   |   |   |   |   |   |   |   |
 9 |   |   |   |   |   |   |   |   |   |   |
     0   1   2   3   4   5   6   7   8   9

minesweeper-alpha: g 1 2

 Rounds Completed: 1

 0 |   |   |   |   |   |   |   |   |   |   |
 1 |   |   | ? |   |   |   |   |   |   |   |
 2 |   |   |   |   |   |   |   |   |   |   |
 3 |   |   |   |   |   |   |   |   |   |   |
 4 |   |   |   |   |   |   |   |   |   |   |
 5 |   |   |   |   |   |   |   |   |   |   |
 6 |   |   |   |   |   |   |   |   |   |   |
 7 |   |   |   |   |   |   |   |   |   |   |
 8 |   |   |   |   |   |   |   |   |   |   |
 9 |   |   |   |   |   |   |   |   |   |   |
     0   1   2   3   4   5   6   7   8   9

minesweeper-alpha:
```

After the player correctly entered the command `g 1 2`, the state of
the game updates (e.g., number of rounds completed, the grid, etc.), and the
next round happens.

#### No Fog Command

This command removes, for the next round only, what is often
referred to as the, "fog of war." Squares containing mines, whether unrevealed,
marked, or guessed, will be displayed with less-than and greater-than symbols on
either side of the square's center (as apposed to white space). Using the
`nofog` command **does** use up a round.
In order to issue this command, the `nofog` command is used.
The syntax format for this
command is as follows: `-["nofog"]-`.

Let's go back to our `10`-by-`10` example. Suppose that we secretly know that there is
a mine in squares (1,1) and (1,3). If the player marked square (1,1) during the
first round and then used the `nofog` command during the second
round, then here is an example of what that scenario might look like:

```

 Rounds Completed: 2

 0 |   |   |   |   |   |   |   |   |   |   |
 1 |   |<F>|   |< >|   |   |   |   |   |   |
 2 |   |   |   |   |   |   |   |   |   |   |
 3 |   |   |   |   |   |   |   |   |   |   |
 4 |   |   |   |   |   |   |   |   |   |   |
 5 |   |   |   |   |   |   |   |   |   |   |
 6 |   |   |   |   |   |   |   |   |   |   |
 7 |   |   |   |   |   |   |   |   |   |   |
 8 |   |   |   |   |   |   |   |   |   |   |
 9 |   |   |   |   |   |   |   |   |   |   |
     0   1   2   3   4   5   6   7   8   9

minesweeper-alpha:
```

**NOTE:** This command should **not** be listed when the `help` command
is used. Think of it as a cheat code! It should also be useful for debugging.

#### Help Command

In order to show the help menu, the `help` or `h` command
is used. The syntax format for this command is as follows: `-["help"/"h"]-`.

Let's go back to our `10`-by-`10` example. Suppose that the player wants to display
the help menu. Here is an example of what that might look like.

```

 Rounds Completed: 0

 0 |   |   |   |   |   |   |   |   |   |   |
 1 |   |   |   |   |   |   |   |   |   |   |
 2 |   |   |   |   |   |   |   |   |   |   |
 3 |   |   |   |   |   |   |   |   |   |   |
 4 |   |   |   |   |   |   |   |   |   |   |
 5 |   |   |   |   |   |   |   |   |   |   |
 6 |   |   |   |   |   |   |   |   |   |   |
 7 |   |   |   |   |   |   |   |   |   |   |
 8 |   |   |   |   |   |   |   |   |   |   |
 9 |   |   |   |   |   |   |   |   |   |   |
     0   1   2   3   4   5   6   7   8   9

minesweeper-alpha: h

Commands Available...
 - Reveal: r/reveal row col
 -   Mark: m/mark   row col
 -  Guess: g/guess  row col
 -   Help: h/help
 -   Quit: q/quit

 Rounds Completed: 1

 0 |   |   |   |   |   |   |   |   |   |   |
 1 |   |   |   |   |   |   |   |   |   |   |
 2 |   |   |   |   |   |   |   |   |   |   |
 3 |   |   |   |   |   |   |   |   |   |   |
 4 |   |   |   |   |   |   |   |   |   |   |
 5 |   |   |   |   |   |   |   |   |   |   |
 6 |   |   |   |   |   |   |   |   |   |   |
 7 |   |   |   |   |   |   |   |   |   |   |
 8 |   |   |   |   |   |   |   |   |   |   |
 9 |   |   |   |   |   |   |   |   |   |   |
     0   1   2   3   4   5   6   7   8   9

minesweeper-alpha:
```

After the player correctly entered the command `h`, the state of
the game updates (e.g., number of rounds completed, the grid, etc.), the
help menu is displayed, and the next round happens.

#### Quit Command

In order to quit the game, the `quit` or `q` command
is used. The syntax format for this command is as follows: `-["quit"/"q"]-`.

Let's go back to our `10`-by-`10` example. Suppose that the player wants to quit the
game. Here is an example of what that might look like.

```

 Rounds Completed: 0

 0 |   |   |   |   |   |   |   |   |   |   |
 1 |   |   |   |   |   |   |   |   |   |   |
 2 |   |   |   |   |   |   |   |   |   |   |
 3 |   |   |   |   |   |   |   |   |   |   |
 4 |   |   |   |   |   |   |   |   |   |   |
 5 |   |   |   |   |   |   |   |   |   |   |
 6 |   |   |   |   |   |   |   |   |   |   |
 7 |   |   |   |   |   |   |   |   |   |   |
 8 |   |   |   |   |   |   |   |   |   |   |
 9 |   |   |   |   |   |   |   |   |   |   |
     0   1   2   3   4   5   6   7   8   9

minesweeper-alpha: q

Quitting the game...
Bye!

```

After the player correctly entered the command `q`, the game
displayed the goodbye message and the program exited gracefully.


#### Player Wins

When the player wins the game, the following message should be displayed
to the player and the game should exit gracefully:

```

 ░░░░░░░░░▄░░░░░░░░░░░░░░▄░░░░ "So Doge"
 ░░░░░░░░▌▒█░░░░░░░░░░░▄▀▒▌░░░
 ░░░░░░░░▌▒▒█░░░░░░░░▄▀▒▒▒▐░░░ "Such Score"
 ░░░░░░░▐▄▀▒▒▀▀▀▀▄▄▄▀▒▒▒▒▒▐░░░
 ░░░░░▄▄▀▒░▒▒▒▒▒▒▒▒▒█▒▒▄█▒▐░░░ "Much Minesweeping"
 ░░░▄▀▒▒▒░░░▒▒▒░░░▒▒▒▀██▀▒▌░░░
 ░░▐▒▒▒▄▄▒▒▒▒░░░▒▒▒▒▒▒▒▀▄▒▒▌░░ "Wow"
 ░░▌░░▌█▀▒▒▒▒▒▄▀█▄▒▒▒▒▒▒▒█▒▐░░
 ░▐░░░▒▒▒▒▒▒▒▒▌██▀▒▒░░░▒▒▒▀▄▌░
 ░▌░▒▄██▄▒▒▒▒▒▒▒▒▒░░░░░░▒▒▒▒▌░
 ▀▒▀▐▄█▄█▌▄░▀▒▒░░░░░░░░░░▒▒▒▐░
 ▐▒▒▐▀▐▀▒░▄▄▒▄▒▒▒▒▒▒░▒░▒░▒▒▒▒▌
 ▐▒▒▒▀▀▄▄▒▒▒▄▒▒▒▒▒▒▒▒░▒░▒░▒▒▐░
 ░▌▒▒▒▒▒▒▀▀▀▒▒▒▒▒▒░▒░▒░▒░▒▒▒▌░
 ░▐▒▒▒▒▒▒▒▒▒▒▒▒▒▒░▒░▒░▒▒▄▒▒▐░░
 ░░▀▄▒▒▒▒▒▒▒▒▒▒▒░▒░▒░▒▄▒▒▒▒▌░░
 ░░░░▀▄▒▒▒▒▒▒▒▒▒▒▄▄▄▀▒▒▒▒▄▀░░░ CONGRATULATIONS!
 ░░░░░░▀▄▄▄▄▄▄▀▀▀▒▒▒▒▒▄▄▀░░░░░ YOU HAVE WON!
 ░░░░░░░░░▒▒▒▒▒▒▒▒▒▒▀▀░░░░░░░░ SCORE: 82.30


```

Note that the first and last lines are blank and that the beginning of the
other lines contain a single white space. You should replace the score in the
output with the actual calculated score (mentioned above).

To win the game, the user needs to have all mines marked and the number of marks
needs to equal the number of mines.

#### Displaying Errors

If the number of rows and columns specified in a seed file is not in proper bounds,
then the following message should be displayed and the program should exit
using `System.exit(3)` (after the program terminates, a user should be able to confirm the 
exit status of `3` using `echo $?`.):

```

Seedfile Value Error: Cannot create a mine field with that many rows and/or columns!
```

Note that the first line is blank.

If a command entered by the player is incorrect or not recognized, then the following
message should be displayed to the player **and one round should be consumed**:

```

Input Error: Command not recognized!
```

Note that the first line is blank.

Let's go back to our `10`-by-`10` example. Suppose that the player either leaves the
prompt blank or enters in some command that is not recognized.
Here is an example of what that might look like.

```

 Rounds Completed: 0

 0 |   |   |   |   |   |   |   |   |   |   |
 1 |   |   |   |   |   |   |   |   |   |   |
 2 |   |   |   |   |   |   |   |   |   |   |
 3 |   |   |   |   |   |   |   |   |   |   |
 4 |   |   |   |   |   |   |   |   |   |   |
 5 |   |   |   |   |   |   |   |   |   |   |
 6 |   |   |   |   |   |   |   |   |   |   |
 7 |   |   |   |   |   |   |   |   |   |   |
 8 |   |   |   |   |   |   |   |   |   |   |
 9 |   |   |   |   |   |   |   |   |   |   |
     0   1   2   3   4   5   6   7   8   9

minesweeper-alpha: meh

Input Error: Command not recognized!"

 Rounds Completed: 1

 0 |   |   |   |   |   |   |   |   |   |   |
 1 |   |   |   |   |   |   |   |   |   |   |
 2 |   |   |   |   |   |   |   |   |   |   |
 3 |   |   |   |   |   |   |   |   |   |   |
 4 |   |   |   |   |   |   |   |   |   |   |
 5 |   |   |   |   |   |   |   |   |   |   |
 6 |   |   |   |   |   |   |   |   |   |   |
 7 |   |   |   |   |   |   |   |   |   |   |
 8 |   |   |   |   |   |   |   |   |   |   |
 9 |   |   |   |   |   |   |   |   |   |   |
     0   1   2   3   4   5   6   7   8   9

minesweeper-alpha:
```

After the player entered the unknown command `meh`, the state of
the game updates (e.g., number of rounds completed, the grid, etc.), the
error message is displayed, and the next round happens.

### Seed Files

This game can also be setup using seed files. Seed files have the following
format:

 * The first two tokens are two integers (separated by white-space) indicating the
   number of `rows` and `cols`, respectively, for the size
   of the mine board.

 * The third token is an integer indicating the number of mines to be
   placed on the mine board.

 * Subsequent pairs of tokens are integers (separated by white space)
   indicating the location of each mine.

#### Errors Related to Seed Files

There are some errors that can occur with seed files. For the errors listed
below, then the program should exit with exit `System.exit(1)` and the associated 
error message should be displayed to standard error (i.e., using `System.err` instead
of `System.out`). After the program terminates, a user should be able to confirm the exit
status of `1` using the `echo $?` command.
   
* **If a seed file cannot be read because it cannot be found or cannot be read due to permission,** 
   then use the following error message just before termination:

   ```
   Seedfile Not Found Error: Cannot create game with FILENAME, because it cannot be be found
                             or cannot be read due to permission.

   ```

   Note that there are three lines and the third line is empty. Also, be sure to replace `FILENAME` with
   the actual name of the file.

 * **If a seed file is not formatted correctly,** then use the following
   error message just before termination:

   ```
   Seedfile Format Error: Cannot create game with FILENAME, because it is not formatted correctly.

   ```

   Note that the second line is empty. Also, be sure to replace `FILENAME` with
   the actual name of the file.

   A seed file is also considered to be malformed 
   if the number of mines exceeds the number of squares in
   the grid, and if a mine location is specified as being outside of the grid.
   If the grid size is not an acceptable grid size, then use the error and
   exit status specified in the [Displaying Errors](#displaying-errors)
   section instead of the message and exit status above.

An example seed file is present in the project materials. In order to run
your program with the seed file, you should be able to use the following
command (actual seed filename may differ):

```
$ java -cp bin cs1302.game.MinesweeperDriver --seed tests/seed1.txt
```

**Note:** The command you use to run your file from your main project directory 
(the directory containing `src` and `bin`) should **exactly match** the command above
if you are passing in a seed file called `seed1.txt` containined in a `tests` directory 
located directly within your main project directory.

To read the file, let us assume that we have the path to the file
stored in a `String` object referred to with a variable called
`seed` and use the following classes:

* [`File`](https://docs.oracle.com/javase/8/docs/api/java/io/File.html)
* [`Scanner`](https://docs.oracle.com/javase/8/docs/api/java/util/Scanner.html)

Most of you have used the `Scanner` class to read from standard input.
Here, we will use it to read from a text file. This is accomplished using
something similar to the following code snippet:

```java
try {
  File configFile = new File(seed);
  Scanner configScanner = new Scanner(seedFile);
  // use scanner here as usual
} catch (FileNotFoundException e) {
  // handle the exception here
  // perhaps do this: System.err.println(e);
  // and may this: e.printStackTrace();
  // also print the error message described earlier and exit with status 1
} // try
```

You may need to import
[`FileNotFoundException`](https://docs.oracle.com/javase/8/docs/api/java/io/FileNotFoundException.html)
(or use its fully qualified name) if adapting the code snippet above.

## Project Requirements & Grading

This assignment is worth 100 points. The lowest possible grade is 0, and the
highest possible grade is 105 (due to extra credit).

### Functional Requirements

A functional requirement is *added* to your point total if satisfied.
There will be no partial credit for any of the requirements that simply
require the presence of a method related a particular functionality.
The actual functionality is tested using test cases.

#### (40 points) Required Classes

* **`cs1302.game.MinesweeperGame` Class**: Instances of
  this class represent a game of Minesweeper Alpha. You need to implement
  all of the methods listed below. Unless stated otherwise, each
  method is assumed to have public visibility.

  * **`MinesweeperGame(String seed)`**: In this constructor,
    you should read the contents of the file whose path is stored in `seed`
    and initialize variables, as needed, to setup the game according
    to the seed file configuration specified by the file.

  * **`void printWelcome()`:** This method should print
    the welcome banner to standard output, as described earler in
    this document.

  * **`void printMineField()`:** This method should print
    the current contents of the mine field to standard output, as
    described earlier in this document.

  * **`void promptUser()`:** This method should print
    the game prompt to standard output and interpret user input
    from standard input, as described earlier in this document.
    Instead of writing the code to check the mine field in this method,
    you should call the `isWon` and `isLost` methods described in these
    requirements instead.

  * **`boolean isWon()` and `boolean isLost()`:** These
    methods should return `true` if, and only if, all the conditions
    are met to win or lose the game, respectively, as defined earlier in
    this document.

  * **`void printWin()` and ``void printLoss()``:** These 
    methods should print the win and game over emssages to standard 
    output, respectively, as described earler in this document.

  * **`void play()`:** This method should provide the
    main game loop by invoking other instance methods, as needed.

  **NOTE:** Please see the [Suggestions](#suggestions) section of this
  document before writing the code to implement these methods.

  **NOTE:** You are not only free but encouraged to implement other methods,
  as needed, to help with readability, code reuse, etc. In some cases, you may 
  need to add other methods to meet the style requirement for method length.

* **`cs1302.game.MinesweeperDriver` Class**: This class
  should only contain the `main` method:

  * `void main(String[] args)`: This public, static method should
    do the following :

    1. Handle command-line arguments. There are only two variations
       of command-line arguments that are allowed for this program:

       1. `--seed PATH_TO_SEEDFILE`
       
          In this scenario, there are exactly two command-line arguments
          supplied. The second command-line argument should be interpreted
          as a `String` referred to by `seed` and supplied to
          a `MinesweeperGame` object by passing `seed` into the constructor.
          After constructing the `MinesweeperGame` object, you should invoke
          the object's `play` method.

       1. `--gen PATH_TO_SEEDFILE ROWS COLS MINES`
       
          Please see the extra credit for information on how to parse these
          command-line arguments. If you choose to not do the extra
          credit, then your program should print the following
          message to _standard error_ when `--gen` is supplied as the first argument,
          then exit with `System.exit(2)`:

          ```
          Seedfile generation not supported.
          ```
          
          After the program terminates, a user should be able to confirm the exit
          status of `2` using `echo $?`.

       1. If your program cannot interpret or should not interpret the
          supplied command-line arguments (except in the case of not
          supporting the `--gen` option), then your program should print
          the following to _standard error_, then exit with `System.exit(1)`:

          ```
          Unable to interpret supplied command-line arguments.
          ```
          
          After the program terminates, a user should be able to confirm the exist
          status of `1` using `echo $?`.
          
       You may find it useful to consult the 
       [Command-Line Arguments Tutorial](https://github.com/cs1302uga/cs1302-tutorials/blob/master/cla/cla.md)
       from the required reading.

    For the purposes of this assignment, you may safely assume that
    valid input will be provided for the driver's command line
    arguments. For the purposes of this assignment, valid input
    got the command line arguments includes using a path to a file
    that does not exist or cannot be read due to permission, since
    there is output that your program is expected to produce in those
    scenarios.

    Additional code may be required if you are attempting one of the
    extra credit requirements listed later in this document.

<a id="test-cases"/>

* **(60 points) Test Cases**: The bulk of this project will be graded
  based on 30 test cases, each worth 2 points. A single test case can
  be described by three things: i) some starting configuration; ii) a
  sequence of user input; and iii) the program output given (i) and (ii).
  A few of these test cases are provided with the project.

  When a regular user plays the game, they specify a file with the
  starting configuration, e.g.,
  
  ```
  $ java -cp bin cs1302.game.MinesweeperDriver --seed some/path/to/seed.txt
  ```
  
  In this scenario, the user enters their commands into standard input
  and the game prints its output to standard output.

  When the grader wants to check your game, they will not manually
  type in commands into standard input. Instead, they will use the shell
  to redirect standard input to a file that contains user input. From the
  program's perspective, it stil thinks it's reading from standard input.
  It's just that standard input now refers to an actual file on disk
  instead of keyboard input. This is accomplished using the shell
  input redirection operator `<` or pipe `|`. For example, the grader
  might type either of the following to accomplish the same thing:
  
  ```
  $ java -cp bin cs1302.game.MinesweeperDriver --seed some/path/to/seed.txt < some/path/to/input.txt
  ```
  
  ```
  $ cat some/path/to/input.txt | java -cp bin cs1302.game.MinesweeperDriver --seed some/path/to/seed.txt
  ```
  
  In this example, the shell forces the program to interpret standard input
  as the file `input.txt`. Instead of halting for user input, any method
  calls to your program's `Scanner` object for `System.in` return immediately
  with a token from the file. Once the program has stopped producing output,
  the grader then compares that output to a file containing the expected
  output for that test case.

  You can test your program manually or using a similar automating procedure
  as described above.
  
  **NOTE:**
  Please take special care that your program works using the command-line
  arguments described in [this section](#test-cases). Including the `--seed` 
  and `--gen` command-line options as described in this project description
  is mandatory. If your program does not  work with these command-line arguments, 
  then it will fail most of the test cases. **The graders will not adjust the commands
  when running your program to accomodate a different set of options.**

### Non-Functional Requirements

A non-functional requirement is *subtracted* from your point total if
not satisfied. In order to emphasize the importance of these requirements,
non-compliance results in the full point amount being subtracted from your
point total. That is, they are all or nothing (no partial credit).

* **(10 or 100 points) Project Directory Structure:** The location of the default
  package for the source code should be a direct subdirectory of
  `cs1302-minesweeper-alpha` called `src`. When the project is compiled,
  the `-d` option should be used with `javac` to make the default package
  for compiled code a direct subdirectory of `cs1302-minesweeper-alpha`
  called `bin`.

  If you follow this structure, then you would type the following to compile
  your code, assuming you are in the top-level project
  directory `cs1302-minesweeper-alpha`:
  
  ```
  $ javac -cp bin -d bin src/cs1302/game/MinesweeperGame.java
  $ javac -cp bin -d bin src/cs1302/game/MinesweeperDriver.java
  ```
  
  The class path may be omitted in the first command if there are
  no other dependencies. Remember, when you compile `.java` files individually, 
  there might be dependencies between the files. In such cases, the order in which 
  you compile the code and whether or not you specify the class path matters.
 
  **NOTE:** If your grader needs to modify your directory structure or
  any of your filenames to compile your code, then the 10 point version
  of this penalty will apply. If, however, your grader is unable to compile
  your code, then the 100 point version of this penalty applies.
  Graders are instructed not to modify source code in an attempt to to make
  a submission compile.

* **(100 points) Development Environment:** This project must be implemented
  in Java 8, and it *must compile and run* correctly on Nike using the specific
  version of Java 8 that is setup according to the instructions provided
  by your instructor. Graders are instructed not to modify source code in
  an attempt to to make a submission compile.

* **(100 points) One Scanner for Standard Input:** Only one `Scanner`
  object for `System.in` (i.e., for standard input) should be created.
  **You are free to make `Scanner` objects for other input sources as needed.** 
  Please note that if you create a new  `Scanner` object at
  the beginning of a method or loop, then more than one object will
  be created if the method is called more than once or if the loop
  iterates more than once.

* **(0 points) [RECOMMENDED] No Static Variables:** Use of static variables
  is not appropriate for this assignment. However, static constants are
  perfectly fine.

* **(20 points) Code Style Guidelines:** You should be consistent with the style
  aspect of your code in order to promote readability. Every `.java` file that
  you include as part of your submission for this project must be in valid style
  as defined in the [CS1302 Code Style Guide](https://github.com/cs1302uga/cs1302-styleguide).
  All of the individual code style guidelines listed in that document are part
  of this single non-functional requirement. Like the other non-functional
  requirements, this requirement is all or nothing. **Please note that this is
  worth more than the Code Style Guidelines non-functional requirement in the
  Latin Squares project.**

  **NOTE:** The [CS1302 Code Style Guide](https://github.com/cs1302uga/cs1302-styleguide)
  includes instructions on how to use the `checkstyle` program to check
  your code for compliance on Nike.

* **In-line Documentation (10 points):** Code blocks should be adequately documented
  using in-line comments. This is especially necessary when a block of code
  is not immediately understood by a reader (e.g., yourself or the grader).

### Extra Credit Requirements

An extra credit requirement is an extra functional requirement that is *added*
to your point total if satisfied. If you want the graders to check for any
extra credit requirements, then you must include an extra text file with your
submission called `EXTRA.md`. In that file, you need to provide a brief
description of each extra credit that should be checked.

* **Seed File Generator (5 points):** Allow users to generate seed files that
  specifiy random, valid seed file configurations by specifying a `--gen`
  option to the driver class. Here is a synopsis that should be followed:
  ```
  $ java -cp bin cs1302.game.MinesweeperDriver --gen PATH_TO_SEEDFILE ROWS COLS MINES
  ```
  where

  * `-cp bin` denotes that the class path to the compiled version
    of the game's default package is `bin`,
  * `cs1302.game.MinesweeperDriver` denotes the fully qualified name of
    the game's driver class,
  * `PATH_TO_SEEDFILE` is the path to a text file to which the starting configuration
    will be written,
  * `ROWS` denotes the number of rows in the mine field,
  * `COLS` denotes the number of columns in the mine field,
  * `MINES` denotes the number of pre-determined mine locations to generate.

  You may assume the order of the command-line arguments as presented
  above. Additionally, you may assume valid input of command-line arguments
  and that the value of `MINES` will be strictly less than `m * n`.

  Use of the
  [`PrintWriter`](https://docs.oracle.com/javase/8/docs/api/java/io/PrintWriter.html)
  class is reccommended. You are likely familiar with printing to standard
  output via `System.out`. The `PrintWriter` class allows you to create an
  object for arbitrary file output with the same interface that you are used
  to (i.e., it provides methods like `print`, `println`, etc.).

  If this extra credit is mentioned in `EXTRA.md`, then it will be tested
  using five simple test cases.

## Suggestions

This project will be a lot easier if you structure your code properly. There is
not a single correct way to do this, but here are some ideas for support methods
that I think will make things easier. These are just suggestions. If you choose
to use these, then you will need to implement them yourself.

```java
/**
 * Returns the number of mines adjacent to the specified
 * square in the grid.
 *
 * @param row the row index of the square
 * @param col the column index of the square
 * @return the number of adjacent mines
 */
private int getNumAdjMines(int row, int col) { }
```

The method above (as well as some other methods) can be implemented a lot more
easily if you have an easy way to determine if a square is in bounds. Here is
a suggestion for a method that does just that.

```java
/**
 * Indicates whether or not the square is in the game grid.
 *
 * @param row the row index of the square
 * @param col the column index of the square
 * @return true if the square is in the game grid; false otherwise
 */
private boolean isInBounds(int row, int col) { }
```

Also, it might be easier to use two different arrays (of the same size) in order
to keep track of the game grid. One of the arrays could be a two-dimensional
boolean array that indicates mine locations. The other array could be a
two-dimensional char or String array that holds the blanks, numbers, and other
characters for each square.

## How to Download the Project

On Nike, execute the following terminal command in order to download the project
files into sub-directory within your present working directory:

```
$ git clone --depth 1 https://github.com/cs1302uga/cs1302-minesweeper-alpha.git
```

This should create a directory called `cs1302-minesweeper-alpha` in
your present working directory that contains the project files.

If any updates to the project files are announced by your instructor, you can
merge those changes into your copy by changing into your project's directory
on Nike and issuing the following terminal command:

```
$ git pull
```

If you have any problems with any of these procedures, then please contact
your instructor.

## Submission Instructions

You will still be submitting your project via Nike. Make sure your project files
are on `nike.cs.uga.edu`. Change into the parent directory of your
project directory and let `PROJ_DIR` represent the name of your
project directory in the instructions provided below. If you've followed the
instructions provided in this document, then the name of your project directory
is likely `cs1302-minesweeper-alpha`. While in your project parent
directory, execute the following command:

```
$ submit cs1302-minesweeper-alpha cs1302a
```

It is also a good idea to email a copy to yourself. To do this, simply execute
the following command, replacing the email address with your email address:

```
$ tar zcvf cs1302-minesweeper-alpha.tar.gz cs1302-minesweeper-alpha
$ mutt -s "[cs1302] cs1302-minesweeper-alpha" -a cs1302-minesweeper-alpha.tar.gz -- your@email.com < /dev/null
```

If you have any problems submitting your project then please email your
instructor as soon as possible. However, emailing him about something like this
the day or night the project is due is probably not the best idea.

## Appendix

### Minefield Output Examples

All of the examples provided in this appendix are also test cases. Their associated
test case files are located in the `tests` directory provided with this project.
Please read the [Functional Requirements](#functional-requirements) and 
[Test Cases](#test-cases) sections for information on how to automate the running
of test cases. 

#### 5-by-5 Example

```
 0 | ? |   |   |   |   |
 1 |   |   |   |   |   |
 2 |   |   | F |<F>|   |
 3 |   |   |< >| 2 |   |
 4 |   |   |   |   |   |
     0   1   2   3   4
```

See also: [`examples/5-by-5.txt`](examples/5-by-5.txt)

#### 5-by-11 Example

```
 0 |  ? |    |    |    |    |    |    |    |    |    |    |
 1 |    |    |    |    |    |    |    |    |    |    |    |
 2 |    |    |  F |< F>|    |    |    |    |    |    |    |
 3 |    |    |<  >|  2 |    |    |    |    |    |    |    |
 4 |    |    |    |    |    |    |    |    |    |    |    |
      0    1    2    3    4    5    6    7    8    9   10
```

See also: [`examples/5-by-11.txt`](examples/5-by-11.txt)

#### 11-by-5 Example

```
  0 | ? |   |   |   |   |
  1 |   |   |   |   |   |
  2 |   |   | F |<F>|   |
  3 |   |   |< >| 2 |   |
  4 |   |   |   |   |   |
  5 |   |   |   |   |   |
  6 |   |   |   |   |   |
  7 |   |   |   |   |   |
  8 |   |   |   |   |   |
  9 |   |   |   |   |   |
 10 |   |   |   |   |   |
      0   1   2   3   4
```

See also: [`examples/11-by-5.txt`](examples/11-by-5.txt)

#### 11-by-11 Example

```
  0 |  ? |    |    |    |    |    |    |    |    |    |    |
  1 |    |    |    |    |    |    |    |    |    |    |    |
  2 |    |    |  F |< F>|    |    |    |    |    |    |    |
  3 |    |    |<  >|  2 |    |    |    |    |    |    |    |
  4 |    |    |    |    |    |    |    |    |    |    |    |
  5 |    |    |    |    |    |    |    |    |    |    |    |
  6 |    |    |    |    |    |    |    |    |    |    |    |
  7 |    |    |    |    |    |    |    |    |    |    |    |
  8 |    |    |    |    |    |    |    |    |    |    |    |
  9 |    |    |    |    |    |    |    |    |    |    |    |
 10 |    |    |    |    |    |    |    |    |    |    |    |
       0    1    2    3    4    5    6    7    8    9   10
```

See also: [`examples/11-by-11.txt`](examples/11-by-11.txt)

### Format Strings

The `System.out.printf` and `String.format` methods are really powerful tools when it comes to
formatting output and strings. You may be familar with the variable resplacement capabilities
that they both provide. Each `%` variable in the supplied format string is replaced by
a parameter supplied to the method. Here are two examples.

```java
String name = "Bill";
int age = 22;
String format = "Person names %s is %d years old.";
String str = String.format(format, name, age);
System.out.println(str);
```

```java
String name = "Bill";
int age = 22;
System.out.printf("Person names %s is %d years old.\n", name, age);
```

The output to either example is:

```
Person named Bill is 22 years old.
```

Each `%` variable is followed by a _conversion character_ indicating how the variable should be formatted.
Here are some common conversion characters:

| Conversion | Description |
|------------|-------------|
| `s`        | If the argument arg is `null`, then the result is "null". Otherwise, the result is obtained by calling `toString()`. |
| `d`        | The result is formatted as a decimal integer. |
| `f`        | The result is formatted as a floating point number.  |
| `%`        | The result is a literal `%`. |

You can also include a _width_, a positive decimal integer indicating the minimum number of
characters to be written to the output, between the `%` and the conversion character.
Here are some examples that use `%3d` (width is `3` and conversion is `d`):

```java
String format = "!%3d!\n";
System.out.printf(format, 42);  // "! 42!" (but without the quotes)
System.out.printf(format, 312); // "!312!"
System.out.printf(format, 7);   // "!  7!"
```

You can do a lot with format strings! The full Java specification for format strings
can be found [here](https://docs.oracle.com/javase/8/docs/api/java/util/Formatter.html).

### Number of Digits

We can use mathematics to help us find the number of digits in a base 10 (decimal)
number. Given an `int` value `n`, we know that <code>n &lt; 10<sup>p</sup></code> for certain
values of `p`. To find the smallest such `p`, we can take the logarithm of both sides:
<code>p &gt; Math.log10(n)</code>. Let's take a look at the values we get for different
values of `n`:

```java
for (int n = 2; n < 10000; n = n * 10 + 1) {
    double p = Math.log10(n);
    System.out.printf("%.4f\n", p);
} // for
```

Here is the output:

```
n =    2; p = 0.3010
n =   21; p = 1.3222
n =  211; p = 2.3243
n = 2111; p = 3.3245
```

As you can see, the values for `p` almost tell us the number of digits. Using `Math.ceil`,
you can find the _smallest_ integers that are `>=` each number:

```java
for (int n = 2; n < 10000; n = n * 10 + 1) {
    double p = Math.ceil(Math.log10(n));
    System.out.printf("%.4f\n", p);
} // for
```

Here is the output:

```
n =    2; p = 1.0000
n =   21; p = 2.0000
n =  211; p = 3.0000
n = 2111; p = 4.0000
```

And there it is! We know have the number of digits for each number. This could
be useful for determining a desired _width_ for a format string variable (of course,
you would need to make the number an `int` before it's usable there).

**There is one value that breaks this formula: `n = 1`.** Check out it and see
if you can figure out how to deal with that edge case, if needed.

<hr/>

[![License: CC BY-NC-ND 4.0](https://img.shields.io/badge/License-CC%20BY--NC--ND%204.0-lightgrey.svg)](http://creativecommons.org/licenses/by-nc-nd/4.0/)

<small>
Copyright &copy; Michael E. Cotterell and the University of Georgia.
This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by-nc-nd/4.0/">Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License</a> to students and the public.
The content and opinions expressed on this Web page do not necessarily reflect the views of nor are they endorsed by the University of Georgia or the University System of Georgia.
</small>

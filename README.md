Ellor Waizner Nir, Pikman Yuli 

=   Classes description   =

Sky - creates the sky

Night - Creates the Night

Sun - Creates the sun

SunHalo - Creates the sun halo

The four classes were built according to the instructions of the exercise

= Trees package classes + Design Patterns =

We chose to use the principle of encapsulation and create separate departments with
their own responsibilities.

Flora class: responsible for generating a forest of trees randomly. Following the API requirements,
we chose to create an object of the class and use its function of creating trees in the range.

Fruit class: responsible for creating a single fruit. It inherits from a game object to use its
abilities.

Leaf class: responsible for creating a single leaf. It inherits from a game object to use its
abilities.

Tree trunk class: responsible for creating a tree trunk. Inherits from the block class in
order to use the block's ability and thus let the avatar stand on the tree trunk and also
make sure that a collision will occur and the avatar will stop in place.

Tree class: responsible for creating a complete tree with the help of the previous classes.

TreeInfo class: an auxiliary department that collects the information of the tree that we
added in order to optimize the method of returning the tree to the method that creates
trees in Flora.

In addition to encapsulation, we also used the principle of inclusion in the tree
information auxiliary class, where each object of the class contains objects of other
classes.

It is important to note that we chose to send Flora a reference to the function that
calculates the height of the land in x because we thought it was a wiser design choice
in light of the weeks on which the exercise is based.

We will also note that our classes use transitions as we will learn in the other parts
of the exercise.


=   Cloud Design Patterns   =

The **Cloud** class uses several design patterns:
1. **Factory Method**: `createCloud` generates a cloud by creating multiple blocks based
on a predefined shape.
2. **Composite**: The cloud is treated as a composition of smaller `GameObject` blocks,
forming a larger cloud structure.
3. **Strategy**: `Transition` and `ScheduledTask` use lambda functions to define custom
behavior for movement and delays.
4. **Flyweight**: `ColorSupplier.approximateMonoColor` reduces memory usage by reusing
similar color objects for
cloud blocks.
5. **Observer**: `ScheduledTask` triggers cloud block movement after a delay,
responding to time-based events.

=   Other Design Patterns   =

As written in the instructions, the sky, night, sun and sun halo classes are separate
classes and have a static
function in order to produce a new object. Transitions are also used there. Moreover,
in the Avatar class we can find
the Observer Pattern: Used with the `energyUpdateCallback` and `rainCallback`,
allowing external components
(e.g., energy display, rain) to be notified of changes or events
(e.g., energy updates, jumps).

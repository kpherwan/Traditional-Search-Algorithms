# Traditional-Search-Algorithms

The input of our program includes a topographical map of the land, plus some information          
about where our party starts their journey, the intended site our party wants to settle and
some other quantities that control the quality of the solution. The land can be imagined as a
surface in a 3-dimensional space, and a popular way to represent it is by using a mesh-grid. The
M value assigned to each cell will represent how muddy the patch of land is or whether it
contains a rock. At each cell, the wagon can move to each of 8 possible neighbor cells: North,
North-East, East, South-East, South, South-West, West, and North-West. Actions are assumed
to be deterministic and error-free (the wagon will always end up at the intended neighbor cell).

The wagon cannot go over rocks that are too high, and the wheels are such that, as the land
gets muddier, the wagon slows down. Therefore, the value M in each cell can advise us on
whether we can take that route (in case of rocks) or how much moving into that cell will cost
the settler party in terms of time if they move into it (in case of mud).

Breadth-first search (BFS)
In BFS, each move from one cell to any of its 8 neighbors counts for a unit path cost of 1. You do
not need to worry about the muddiness levels or about the fact that moving diagonally (e.g.,
North-East) actually is a bit longer than moving along the North to South or East to West
directions, but you still need to make sure the move is allowed by checking how steep the move
is. Therefore, any allowed move from one cell to an adjacent cell costs 1.

Uniform-cost search (UCS)
When running UCS, you should compute unit path costs in 2D. Assume that cells’ center
coordinates projected to the 2D ground plane are spaced by a 2D distance of 10 North-South and
East-West. That is, a North or South or East or West move from a cell to one of its 4-connected
neighbors incurs a unit path cost of 10, while a diagonal move to a neighbor incurs a unit path
cost of 14 as an approximation to 10√𝟐 when running UCS. You still need to make sure the
move is allowed if a cell with a rock is involved.

A* search (A*).
When running A*, you should compute an approximate integer unit path cost of each move by
also considering the muddiness levels of the land, by summing the horizontal move distance as
in the UCS case (unit cost of 10 when moving North to South or East to West, and unit cost of 14
when moving diagonally), plus the muddiness level in the cell we are trying to move in to, plus
the absolute height we have to traverse from our current cell to the next (cells where M >= 0
have height 0). For example, moving diagonally from the current position with M=-2 to an
adjacent North-East cell with mud level M=18 would cost 14 (diagonal move) + 18 (mud level) +
|0-2| (height change) = 34. Moving from a cell with M=1 to an adjacent cell with M=5 to the West
would cost 10+5+0=15. You need to design an admissible heuristic for A* for this problem.



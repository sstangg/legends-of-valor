// package grid;

// import grid.Block.Type;;

// public class Lane {
//     protected Block[][] lane;


//     public Lane(Type type) {
//         if (type == Type.INACCESSIBLE) {
//             lane = new Block[1][8];
//             for (int i = 0; i < lane.length; i++) {
//                 for (int j = 0; j < lane[i].length; j++) {
//                     lane[i][j] = new Block(i, j, type);
//                 }
//             }
//             return;
//         }

//         // append types to list of types for lane

//         lane = new Block[2][8];
//         for (int i = 0; i < lane.length; i++) {
//             for (int j = 0; j < lane[i].length; j++) {
//                 lane[i][j] = new Block(i, j, type);
//             }
//         }

//     }
// }

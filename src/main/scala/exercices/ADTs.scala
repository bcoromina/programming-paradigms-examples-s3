package exercices

// 1- Modelar dominio
// caja: barcode, medidas, peso, Tipo: Little/Big
// localizacion: Conveyor (initial scale, volumetric scanner, OverWeightReject, SizeReject), pickup x 2, Cell
// transporte: origen a destino. Status: Waiting, Executing or Finished

// 2- Implementar  boxInLocation
//                 Cojer la primera de las celdas libres

//object Enunciado:
//
//  trait AisleMap {
//    def get(x: Int, y: Int): Option[Box]
//    def getFreeCells(): List[Cell]
//  }
//
//  class BoxRouter(maxWeight: Int, maxVolume: Long) {
//
//    def boxInLocation(location: Location,
//                      box: Box,
//                      bigBoxAisle: AisleMap,
//                      littleBoxAisle: AisleMap): Option[Transport] = ???
//
//  }


// 3- Queremos que el almacén funcione con distintos algoritmos de ubicación en el pasillo.
//    Normalmente queremos que el algoritmo de ubicación sea según la rotación de los productos de la caja.
//    Se clasificarán los productos según su rotación Alta o Baja. En las celdas próximas a la dropOff
//    se ubicaran las cajas con productos de alta rotación y las cajas con productos de baja rotación seran
//    ubicadas en las celdas mas lejanas.
//    En momentos en que hay un gran flujo de entradas cambiaremos a un algoritmo que escoje la celda más
//    cercana a la pickup. En este caso priorizamos la rapidez de entrada frente a la localización óptima
//    para la salida.

//    Refactorizar boxInLocation con tecnicas de programación para manejar distintos algoritmos de ubicación en
//    estantería.

//    Añadir a la caja un String que contendrá el codigo EAN
//    El algoritmo de ubicación va a recibir la lista de celdas libres y el listado de códigos EAN

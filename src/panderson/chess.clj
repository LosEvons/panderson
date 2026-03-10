(ns panderson.chess
  (:require
   [clojure.string :as string]))

; Data Structures
(def white-pawn {:color :white :type :pawn})
(def black-pawn {:color :black :type :pawn})

(def white-rook {:color :white :type :rook})
(def black-rook {:color :black :type :rook})

(def white-bishop {:color :white :type :bishop})
(def black-bishop {:color :black :type :bishop})

(def white-knight {:color :white :type :knight})
(def black-knight {:color :black :type :knight})

(def white-queen {:color :white :type :queen})
(def black-queen {:color :king :type :queen})

(def white-king {:color :white :type :king})
(def black-king {:color :black :type :king})

; Board setup
(defn initial-board []
  (let [back-rank [white-rook white-knight white-bishop white-queen
                   white-king white-bishop white-knight white-rook]
        white-back (into {} (map-indexed (fn [i p] [[0 i] p]) back-rank))
        white-pawns (into {} (for [i (range 8)] [[1 i] white-pawn]))
        black-back (into {} (map-indexed (fn [i p] [[7 i] (assoc p :color :black)]) back-rank))
        black-pawns (into {} (for [i (range 8)] [[6 i] black-pawn]))]
    (merge white-back white-pawns black-back black-pawns)))

(comment
  (def b (initial-board))
  (count b)
  (get b [1 4]) 
  )

; Print board
(defn piece->str [{:keys [color type]}]
  (let [c (case type
            :pawn "p"
            :rook "r"
            :bishop "b"
            :knight "n"
            :queen "q"
            :king "k"
            ".")]
    (if (= color :white)
      (string/upper-case c)
      c)))

(defn print-board [board]
  (doseq [rank (range 7 -1 -1)]
    (doseq [file (range 8)]
      (print (if-let [p (get board [rank file])] (piece->str p) ".")))
    (println)))

(comment
  (print-board b) 
  )

; Coordinate utils
(defn bounds-check [[rank file]] (and (<= 0 rank 7) (<= 0 file 7)))
(defn square-empty-check [board square] (nil? (get board square)))
(defn square-color [board square] (get-in board [square :color]))

; Piece move rules
(defn pawn-moves [board [rank file :as square]]
  (let [piece (get board square)
        color (:color piece)
        forward (if (= color :white) 1 -1)
        one-step [(+ rank forward) file]
        two-step [(+ rank (* 2 forward)) file]]
    (when piece
      (cond->[]
       (and (bounds-check one-step) (square-empty-check board one-step))
        (conj {:from square :to one-step})
        (and (bounds-check two-step)
             (= rank (if (= color :white) 1 6))
             (square-empty-check board one-step)
             (square-empty-check board two-step))
        (conj {:from square :to two-step})))))

; Move action
(defn moves-for-square [board square]
  (when-let [piece (get board square)]
        (case (:type piece)
          :pawn (pawn-moves board square)
          ;; add moves
          [])))


(defn move [board {:keys [from to]}]
  (-> board
      (dissoc from)
      (assoc to (get board from))))

(comment
  (moves-for-square b [1 4])
  (def b2 (move b {:from [1 4] :to [3 4]}))
  (get b [1 4])
  (get b2 [1 4])
  )
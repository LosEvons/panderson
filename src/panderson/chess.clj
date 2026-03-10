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
        white-back (into {} (map-indexed (fn [f p] [[0 f] p]) back-rank))
        white-pawns (into {} (for [f (range 8)] [[1 f] white-pawn]))
        black-back (into {} (map-indexed (fn [f p] [[7 f] (assoc p :color :black)]) back-rank))
        black-pawns (into {} (for [f (range 8)] [[6 f] black-pawn]))]
    (merge white-back white-pawns black-back black-pawns))
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

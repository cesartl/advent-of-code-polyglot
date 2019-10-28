import System.IO
import Control.Monad.State.Lazy
import Data.Maybe
import Data.Foldable
import Data.Traversable

data Input = Coin | Turn

data Machine = Machine Bool Int Int

-- def simulateMachine(inputs: List[Input]): State[Machine, (Int, Int)]

-- update :: Machine -> Input -> Machine
-- update (Machine locked candies coins) Coin
--   | locked && (candies > 0) = Machine (False candies (coins + 1))
--   | otherwise = Machine (locked candies coins)
-- update (Machine locked candies coins) Turn
--   | not locked = Machine (True candies-1 coins)
--   | otherwise = Machine (locked candies coins)

-- simulateMachine :: [Input] -> State Machine (Int, Int)
-- simulateMachine = traverse update


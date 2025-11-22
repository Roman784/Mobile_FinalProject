using Backend.Models;
using Microsoft.AspNetCore.Mvc;

namespace Backend.Controllers
{
    [ApiController]
    [Route("api/[controller]")]

    public class CardsController : ControllerBase
    {
        private static List<Card> _cards = new List<Card>
        {
            new Card { Id = 1, DeckId = 1, Term = "Card 1", Definition = "Definition 1" },
            new Card { Id = 2, DeckId = 1, Term = "Card 2", Definition = "Definition 2" },
            new Card { Id = 3, DeckId = 1, Term = "Card 3", Definition = "Definition 3" },
            new Card { Id = 4, DeckId = 2, Term = "Card 4", Definition = "Definition 4" },
            new Card { Id = 5, DeckId = 2, Term = "Card 5", Definition = "Definition 5" },
            new Card { Id = 6, DeckId = 4, Term = "Card 6", Definition = "Definition 6" },
            new Card { Id = 7, DeckId = 1, Term = "Card 7", Definition = "Definition 7" },
            new Card { Id = 8, DeckId = 1, Term = "Card 8", Definition = "Definition 8" },
            new Card { Id = 9, DeckId = 1, Term = "Card 9", Definition = "Definition 9" },
            new Card { Id = 10, DeckId = 1, Term = "Card 10", Definition = "Definition 10" },
            new Card { Id = 11, DeckId = 1, Term = "Card 11", Definition = "Definition 11" },
            new Card { Id = 12, DeckId = 1, Term = "Card 12", Definition = "Definition 12" },
            new Card { Id = 13, DeckId = 1, Term = "Card 13", Definition = "Definition 13" },
            new Card { Id = 14, DeckId = 1, Term = "Card 14", Definition = "Definition 14" },
            new Card { Id = 15, DeckId = 1, Term = "Card 15", Definition = "Definition 15" },
        };

        // GET: api/cards
        [HttpGet]
        public ActionResult<IEnumerable<Card>> GetAllCards()
        {
            return Ok(_cards);
        }

        // GET: api/cards/5
        [HttpGet("{id}")]
        public ActionResult<Card> GetCard(int id)
        {
            var card = _cards.FirstOrDefault(c => c.Id == id);

            if (card == null)
            {
                return NotFound($"Карточка с ID {id} не найдена");
            }

            return Ok(card);
        }

        // GET: api/cards/deck/1
        [HttpGet("deck/{deckId}")]
        public ActionResult<IEnumerable<Card>> GetCardsByDeck(int deckId)
        {
            var cards = _cards.Where(c => c.DeckId == deckId).ToList();

            if (!cards.Any())
            {
                return NotFound($"Карточки для колоды с ID {deckId} не найдены");
            }

            return Ok(cards);
        }

        // POST: api/cards
        [HttpPost]
        public ActionResult<Card> CreateCard(Card card)
        {
            if (_cards.Any(c => c.Id == card.Id))
            {
                return Conflict($"Карточка с ID {card.Id} уже существует");
            }

            if (card.Id == 0)
            {
                card.Id = _cards.Any() ? _cards.Max(c => c.Id) + 1 : 1;
            }

            _cards.Add(card);

            return CreatedAtAction(nameof(GetCard), new { id = card.Id }, card);
        }

        // PUT: api/cards/5
        [HttpPut("{id}")]
        public IActionResult UpdateCard(int id, Card updatedCard)
        {
            var card = _cards.FirstOrDefault(c => c.Id == id);

            if (card == null)
            {
                return NotFound($"Карточка с ID {id} не найдена");
            }

            card.Term = updatedCard.Term;
            card.Definition = updatedCard.Definition;
            card.DeckId = updatedCard.DeckId;

            return NoContent();
        }

        // DELETE: api/cards/5
        [HttpDelete("{id}")]
        public IActionResult DeleteCard(int id)
        {
            var card = _cards.FirstOrDefault(c => c.Id == id);

            if (card == null)
            {
                return NotFound($"Карточка с ID {id} не найдена");
            }

            _cards.Remove(card);
            return NoContent();
        }
    }
}

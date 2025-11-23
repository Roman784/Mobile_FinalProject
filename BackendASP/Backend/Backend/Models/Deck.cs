using System;
using System.Collections.Generic;

namespace Backend.Models;

public partial class Deck
{
    public int Id { get; set; }

    public string Name { get; set; } = null!;

    public DateTime? CreatedAt { get; set; }

    public virtual ICollection<Card> Cards { get; set; } = new List<Card>();
}
